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
package com.softwareonpurpose.traceability4test;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/***
 * CoverageReport accepts entries for each test executed, including the test description, scenario description,
 * and requirement description.  Only the test description is required.
 * A requirement can be a combination of Inter-application requirement (crosses application boundaries)
 * and Intra-application requirement, pipe separated (i.e. [Inter-application|Intra-application]).
 * A CoverageReport is instantiated with the name of the file to which the report is to be written.
 * The list of entries is generated as a report, and written to the filename provided.
 * The report is aggregated by inter-application requirement, intra-application requirement, scenario, and test.
 */
@SuppressWarnings("WeakerAccess")
public class CoverageReport {
    private final String reportSubject;
    private final String applicationCoverageFilename;
    private final String requirementsCoverageFilename;
    private SubjectCoverage subjectCoverage;
    private Set<AppRequirement> requirementsCoverage = new TreeSet<>();

    private CoverageReport(String reportSubject) {
        this.reportSubject = reportSubject;
        subjectCoverage = SubjectCoverage.construct(reportSubject);
        this.applicationCoverageFilename = String.format("%s.application.rpt", reportSubject);
        this.requirementsCoverageFilename = String.format("%s.requirements.rpt", reportSubject);
    }

    /***
     * Get an instance of CoverageReport
     * @param testSubject Name of file to which report will be written
     * @return Instance of CoverageReport
     */
    public static CoverageReport construct(String testSubject) {
        return new CoverageReport(testSubject);
    }

    /***
     * Add an entry for a test, the scenario in which it was executed, and the requirement covered
     * @param test Name of the test executed
     * @param scenario Description of the data scenario in which the test was executed
     * @param requirement Description of the requirement covered
     */
    public void addEntry(String test, String scenario, String requirement) {
        if (test == null || test.isEmpty()) {
            return;
        }
        SubjectCoverage coverageEntry = addSubjectCoverage(test, scenario);
        if (requirement == null || requirement.isEmpty()) {
            return;
        }
        requirementsCoverage.add(AppRequirement.construct(requirement, coverageEntry));
    }

    private SubjectCoverage addSubjectCoverage(String test, String scenario) {
        ExecutedTest executedTest = (scenario == null || scenario.isEmpty())
                ? ExecutedTest.construct(test) : ExecutedTest.construct(test, scenario);
        SubjectCoverage coverageEntry = SubjectCoverage.construct(reportSubject, executedTest);
        subjectCoverage.merge(coverageEntry);
        return coverageEntry;
    }

    public void addEntry(String test, String scenario, String... requirement) {
        String[] requirements = requirement == null ? new String[]{} : requirement;
        for (String aRequirement : requirements) {
            if (aRequirement != null && !aRequirement.isEmpty()) {
                addEntry(test, scenario, aRequirement);
            }
        }
    }

    public void addEntry(String test, String scenario) {
        if (test == null || test.isEmpty()) {
            return;
        }
        ExecutedTest executedTest = (scenario == null || scenario.isEmpty())
                ? ExecutedTest.construct(test)
                : ExecutedTest.construct(test, scenario);
        subjectCoverage.addTest(executedTest);
    }

    public void addEntry(String test) {
        addEntry(test, null, (String) null);
    }

    /***
     * Write the coverage report to file.  Any existing file with the same name will be deleted.
     */
    public void write() {
        deleteReportFiles();
        createReportFiles();
        writeReportFiles();
    }

    private void writeReportFiles() {
        StringBuilder report = new StringBuilder(
                String.format("{\"application_coverage\"%s}", String.format(":[%s]", subjectCoverage.toString())));
        File file = new File(applicationCoverageFilename);
        writeReport(report.toString(), file);
        StringBuilder requirementReport = new StringBuilder();
        for (AppRequirement requirement : requirementsCoverage) {
            requirementReport.append(String.format("%s,", requirement.toString()));
        }
        if (requirementReport.lastIndexOf(",") > -1) {
            requirementReport.deleteCharAt(requirementReport.lastIndexOf(","));
        }
        String reportDetailElement =
                requirementReport.length() == 0 ? "" : String.format(":[%s]", requirementReport.toString());
        report = new StringBuilder(
                String.format("{\"requirements_coverage\"%s}", reportDetailElement));
        file = new File(requirementsCoverageFilename);
        writeReport(report.toString(), file);
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
        List<String> fileList = Arrays.asList(applicationCoverageFilename, requirementsCoverageFilename);
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
        List<String> fileList = Arrays.asList(applicationCoverageFilename, requirementsCoverageFilename);
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
