/*Copyright 2018 Craig A. Stockton

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/
package com.softwareonpurpose.coverage4test;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/***
 * CoverageReport accepts entries for each test executed, including each test, scenarios executed,
 * and requirements covered.  Instantiated with the filename to which the compiled report is to be written.
 */
@SuppressWarnings("WeakerAccess")
public class CoverageReport {
    private static final String REPORT_FILE_PATHNAME_FORMAT = "%s/%s.%s.rpt";
    private final String reportSubject;
    private final String subjectCoverageFilename;
    private final String requirementsCoverageFilename;
    private final String reportDirectory;
    String REPORT_TYPE_SYSTEM = "system";
    String REPORT_TYPE_REQUIREMENTS = "requirements";
    private SubjectCoverage subjectCoverage;
    private Map<String, SystemRequirement> requirementsCoverage = new HashMap<>();

    private CoverageReport(String reportSubject) {
        reportDirectory = "./reports";
        this.reportSubject = reportSubject;
        this.subjectCoverage = SubjectCoverage.getInstance(reportSubject);
        this.subjectCoverageFilename = String.format(REPORT_FILE_PATHNAME_FORMAT, reportDirectory, reportSubject, REPORT_TYPE_SYSTEM);
        this.requirementsCoverageFilename = String.format(REPORT_FILE_PATHNAME_FORMAT, reportDirectory, reportSubject, REPORT_TYPE_REQUIREMENTS);
    }

    /***
     * Get an instance of CoverageReport
     * @param testSubject String description of a test subject
     * @return CoverageReport instance
     */
    public static CoverageReport getInstance(String testSubject) {
        return new CoverageReport(testSubject);
    }

    /***
     * Add the description of a test
     * @param test String test description
     */
    public void addEntry(String test) {
        addEntry(test, null, (String) null);
    }

    /***
     * Add the description of a test and data scenario (test description is required)
     * @param test String test description
     * @param scenario String data scenario (e.g. JSON)
     */
    public void addEntry(String test, String scenario) {
        if (test == null || test.isEmpty()) {
            return;
        }
        ExecutedTest executedTest = (scenario == null || scenario.isEmpty())
                ? ExecutedTest.getInstance(test)
                : ExecutedTest.getInstance(test, scenario);
        subjectCoverage.addTest(executedTest);
    }

    /***
     * Add the description of a test and scenario, and any number of requirement IDs
     * @param test String test description
     * @param scenario String data scenario (e.g. JSON)
     * @param requirement String requirement IDs
     */
    public void addEntry(String test, String scenario, String... requirement) {
        String[] requirements = requirement == null ? new String[]{} : requirement;
        addEntry(test, scenario);
        for (String aRequirement : requirements) {
            if (aRequirement != null && !aRequirement.isEmpty()) {
                addEntry(test, scenario, aRequirement);
            }
        }
    }

    /***
     * Write the coverage reports to files (system and requirement coverage reports).
     * Existing files are overwritten.
     */
    public void write() {
        deleteReportFiles();
        createReportFiles();
        writeReportFiles();
    }

    private void addEntry(String test, String scenario, String requirement) {
        if (test == null || test.isEmpty()) {
            return;
        }
        SubjectCoverage coverageEntry = addSubjectCoverage(test, scenario);
        if (requirement == null || requirement.isEmpty()) {
            return;
        }
        if (requirementsCoverage.containsKey(requirement)) {
            requirementsCoverage.get(requirement).addSubjectCoverage(coverageEntry);
        } else {
            requirementsCoverage.put(requirement, SystemRequirement.getInstance(requirement, coverageEntry));
        }
    }

    private SubjectCoverage addSubjectCoverage(String test, String scenario) {
        ExecutedTest executedTest = (scenario == null || scenario.isEmpty())
                ? ExecutedTest.getInstance(test) : ExecutedTest.getInstance(test, scenario);
        SubjectCoverage coverageEntry = SubjectCoverage.getInstance(reportSubject, executedTest);
        subjectCoverage.merge(coverageEntry);
        return coverageEntry;
    }

    private void writeReportFiles() {
        writeSystemReport();
        writeRequirementsReport();
    }

    private void writeRequirementsReport() {
        String reportHeader = "{\"requirements_coverage\"%s}";
        writeReport(String.format(reportHeader, compileRequirementsReport()), new File(requirementsCoverageFilename));
    }

    private String compileRequirementsReport() {
        StringBuilder requirementReport = new StringBuilder();
        Collection<SystemRequirement> sortedRequirements = requirementsCoverage.values().stream().sorted().collect(Collectors.toList());
        for (SystemRequirement requirement : sortedRequirements) {
            requirementReport.append(String.format("%s,", requirement.toString()));
        }
        if (requirementReport.lastIndexOf(",") > -1) {
            requirementReport.deleteCharAt(requirementReport.lastIndexOf(","));
        }
        return requirementReport.length() == 0 ? ":" : String.format(":[%s]", requirementReport.toString());
    }

    private void writeSystemReport() {
        String reportHeader = "{\"system_coverage\"%s}";
        writeReport(String.format(reportHeader, String.format(":[%s]", subjectCoverage.toString())), new File(subjectCoverageFilename));
    }

    private void writeReport(String report, File file) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(report);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createReportFiles() {
        List<String> fileList = Arrays.asList(subjectCoverageFilename, requirementsCoverageFilename);
        File reportDirectoryFile = new File(reportDirectory);
        try {
            if (reportDirectoryFile.exists() || reportDirectoryFile.mkdirs()) {
                for (String filename : fileList) {
                    File file = new File(filename);
                    if (!file.createNewFile()) {
                        String errorMessage = String.format("Unable to write report file %s", filename);
                        LoggerFactory.getLogger(this.getClass()).error(errorMessage);
                    }
                }
            } else {
                String errorMessage = String.format("Unable to create report directory %s", reportDirectory);
                LoggerFactory.getLogger(this.getClass()).error(errorMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteReportFiles() {
        List<String> fileList = Arrays.asList(subjectCoverageFilename, requirementsCoverageFilename);
        for (String filename : fileList) {
            File file = new File(filename);
            if (file.exists()) {
                if (!file.delete()) {
                    String errorMessage = String.format("Unable to delete report file %s", filename);
                    LoggerFactory.getLogger(this.getClass()).error(errorMessage);
                }
            }
        }
    }
}
