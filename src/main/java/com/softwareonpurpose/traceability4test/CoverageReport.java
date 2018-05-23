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
import java.util.*;
import java.util.stream.Collectors;

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
    protected final static String TRACEABILITY_TITLE = "REQUIREMENTS TRACEABILITY REPORT:";
    protected final static String COVERAGE_TITLE = "APPLICATION COVERAGE REPORT";
    private final static String TITLE_FORMAT = "%s%n";
    private final static String INTER_APPLICATION_FORMAT = "%n    %s";
    private final static String INTRA_APPLICATION_FORMAT = "%n        %s";
    private final static String TEST_FORMAT = "%n            %s";
    private final static String SCENARIO_FORMAT = "%n                %s";
    private final String reportSubject;
    private final List<String> reportedTests = new ArrayList<>();
    private final List<String> reportedIntraApplicationRequirements = new ArrayList<>();
    private final List<String> reportedInterApplicationRequirements = new ArrayList<>();
    private final List<ReportEntry> entryList = new ArrayList<>();
    private final String applicationCoverageFilename;
    private final String requirementsCoverageFilename;
    private String filename;
    private String reportType = "coverage";
    private String report;
    private StringBuilder compiledContent;
    private SubjectCoverage subjectCoverage;

    private CoverageReport(String reportSubject) {
        this.reportSubject = reportSubject;
        subjectCoverage = SubjectCoverage.create(reportSubject);
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
        subjectCoverage.addTest(ExecutedTest.construct(test));
        String[] requirements = requirement == null ? new String[0] : requirement.split("\\.");
        String interAppRequirement = requirements.length == 2 ? requirements[0] : null;
        String intraAppRequirement = requirements.length == 2
                ? requirements[1] : requirements.length == 1 ? requirements[0] : null;
        entryList.add(ReportEntry.create(interAppRequirement, intraAppRequirement, test, scenario));
    }

    /***
     * Add entry covering multiple requirements
     * @param test Name of test executed
     * @param scenario Description of the data scenario in which the test was executed
     * @param requirements Comma-delimited list of descriptions of requirements covered
     */
    public void addEntries(String test, String scenario, String requirements) {
        List<String> requirementsList = requirements == null
                ? Collections.singletonList(null)
                : Arrays.stream(requirements.replace(" ", "").split(",")).collect(Collectors.toList());
        for (String requirement : requirementsList) {
            if (requirement != null && !requirement.equals("")) {
                addEntry(test, scenario, requirement);
            }
        }
    }

    /***
     * Write the coverage report to file.  Any existing file with the same name will be deleted.
     */
    public void write() {
        compileReport();
        deleteReportFiles();
        createReportFiles();
        writeReportFiles();
    }

    private void compileReport() {
        setReportType();
        String reportTitle = "coverage".equals(reportType) ? COVERAGE_TITLE : TRACEABILITY_TITLE;
        compiledContent = new StringBuilder(String.format(TITLE_FORMAT, reportTitle));
        List<ReportEntry> sortedEntries =
                new ArrayList<>(new HashSet<>(entryList)).stream()
                        .sorted().collect(Collectors.toList());
        for (ReportEntry entry : sortedEntries) {
            conditionallyAppendInterApplicationRequirement(entry.getInterAppRequirement());
            conditionallyAppendIntraApplicationRequirement(entry.getIntraAppRequirement());
            conditionallyAppendTest(entry.getTestName());
            appendScenario(entry.getScenario());
        }
        report = compiledContent.toString();
    }

    private void setReportType() {
        for (ReportEntry entry : entryList) {
            if (entry.includesRequirement()) {
                reportType = "traceability";
                break;
            }
        }
        filename = String.format("%s.%s.rpt", reportSubject, reportType);
    }

    private void appendScenario(String scenario) {
        boolean isScenarioAvailable = scenario != null;
        if (isScenarioAvailable) {
            compiledContent.append(String.format(SCENARIO_FORMAT, scenario));
        }
    }

    private void conditionallyAppendTest(String test) {
        if (!reportedTests.contains(test)) {
            compiledContent.append(String.format(TEST_FORMAT, test));
            reportedTests.add(test);
        }
    }

    private void conditionallyAppendIntraApplicationRequirement(String intraApplicationRequirement) {
        boolean isIntraSystemRequirementAvailable = intraApplicationRequirement != null;
        if (isIntraSystemRequirementAvailable && !reportedIntraApplicationRequirements.contains
                (intraApplicationRequirement)) {
            reportedTests.clear();
            compiledContent.append(String.format(INTRA_APPLICATION_FORMAT, intraApplicationRequirement));
            reportedIntraApplicationRequirements.add(intraApplicationRequirement);
        }
    }

    private void conditionallyAppendInterApplicationRequirement(String interApplicationRequirement) {
        boolean isInterSystemRequirementAvailable = interApplicationRequirement != null;
        if (isInterSystemRequirementAvailable && !reportedInterApplicationRequirements.contains
                (interApplicationRequirement)) {
            reportedIntraApplicationRequirements.clear();
            compiledContent.append(String.format(INTER_APPLICATION_FORMAT, interApplicationRequirement));
            reportedInterApplicationRequirements.add(interApplicationRequirement);
        }
    }

    private void writeReportFiles() {
        String report = String.format("{\"application_coverage\"%s}", String.format(":[%s]", subjectCoverage.toString()));
        File file = new File(applicationCoverageFilename);
        writeReport(report, file);
        report = "{\"requirements_coverage\"}";
        file = new File(requirementsCoverageFilename);
        writeReport(report, file);
        file = new File(filename);
        writeReport(this.report, file);
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
        List<String> fileList = Arrays.asList(filename, applicationCoverageFilename, requirementsCoverageFilename);
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
        List<String> fileList = Arrays.asList(filename, applicationCoverageFilename, requirementsCoverageFilename);
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
