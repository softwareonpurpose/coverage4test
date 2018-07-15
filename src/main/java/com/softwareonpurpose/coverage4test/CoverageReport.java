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
 * CoverageReport accepts entries for each test executed, including the test test, scenario test,
 * and requirement test.  Only the test test is required.
 * A requirement can be a combination of Inter-application requirement (crosses application boundaries)
 * and Intra-application requirement, pipe separated (i.e. [Inter-application|Intra-application]).
 * A CoverageReport is instantiated with the name of the file to which the report is to be written.
 * The list of entries is generated as a report, and written to the filename provided.
 * The report is aggregated by inter-application requirement, intra-application requirement, scenario, and test.
 */
@SuppressWarnings("WeakerAccess")
public class CoverageReport {
    private final String reportSubject;
    private final String subjectCoverageFilename;
    private final String requirementsCoverageFilename;
    private SubjectCoverage subjectCoverage;
    private Map<String, SystemRequirement> requirementsCoverage = new HashMap<>();

    private CoverageReport(String reportSubject) {
        this.reportSubject = reportSubject;
        this.subjectCoverage = SubjectCoverage.getInstance(reportSubject);
        this.subjectCoverageFilename = String.format("%s.application.rpt", reportSubject);
        this.requirementsCoverageFilename = String.format("%s.requirements.rpt", reportSubject);
    }

    /***
     * Construct an instance of CoverageReport
     * @param testSubject Description of what is being tested
     * @return Instance of CoverageReport
     */
    public static CoverageReport getInstance(String testSubject) {
        return new CoverageReport(testSubject);
    }

    /***
     * Add a test to the coverage reports
     * @param test Description of test (e.g. test method name)
     */
    public void addEntry(String test) {
        addEntry(test, null, (String) null);
    }

    /***
     * Add a test to coverage reports along with specific data scenario
     * @param test Description of test (e.g. test method name)
     * @param scenario Data scenario (e.g. json of test data objects)
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
     * Add a test to coverage reports along with specific data scenario and requirements verified
     * @param test Description of test (e.g. test method name
     * @param scenario Data scenario (e.g. json of test data objects)
     * @param requirement Any number of Requirement IDs (e.g. user story ids)
     */
    public void addEntry(String test, String scenario, String... requirement) {
        String[] requirements = requirement == null ? new String[]{} : requirement;
        for (String aRequirement : requirements) {
            if (aRequirement != null && !aRequirement.isEmpty()) {
                addEntry(test, scenario, aRequirement);
            }
        }
    }

    /***
     * Write the coverage reports to file.  Any existing files with the same names will be deleted.
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
        writeApplicationReport();
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
        return requirementReport.length() == 0 ? "" : String.format(":[%s]", requirementReport.toString());
    }

    private void writeApplicationReport() {
        String reportHeader = "{\"application_coverage\"%s}";
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
        for (String filename : fileList) {
            File file = new File(filename);
            try {
                if (!file.createNewFile()) {
                    String errorMessage = String.format("Unable to write report file %s", filename);
                    LoggerFactory.getLogger(this.getClass()).error(errorMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
