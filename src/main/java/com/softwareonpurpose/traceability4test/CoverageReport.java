/*Copyright 2017 Craig A. Stockton

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
    protected final static String reportTitle = "TRACEABILITY REPORT:";
    private final static String TITLE_FORMAT = "%s%n";
    private final static String INTER_SYSTEM_FORMAT = "%n  %s";
    private final static String INTRA_SYSTEM_FORMAT = "%n    %s";
    private final static String TEST_FORMAT = "%n      %s";
    private final static String SCENARIO_FORMAT = "%n        %s";
    private final static int INTER_SYSTEM_REQUIREMENT_INDEX = 0;
    private final static int INTRA_SYSTEM_REQUIREMENT_INDEX = 1;
    private final static int TEST_INDEX = 2;
    private final static int SCENARIO_INDEX = 3;
    private final static String NOT_AVAILABLE = " N/A";
    private final String filename;
    private List<String> testScenarios = new ArrayList<>();
    private List<String> reportedTests = new ArrayList<>();
    private List<String> reportedIntraSystemRequirements = new ArrayList<>();
    private List<String> reportedInterSystemRequirements = new ArrayList<>();
    private StringBuilder compiledContent;

    private CoverageReport(String target) {
        this.filename = String.format("%s.coverage.rpt", target);
    }

    /***
     * Get an instance of CoverageReport
     * @param filename Name of file to which report will be written
     * @return Instance of CoverageReport
     */
    public static CoverageReport getInstance(String filename) {
        return new CoverageReport(filename);
    }

    /***
     * Compile the lists of Requirements, Scenarios and Tests into content for a coverage report
     * @return String containing formatted coverage report
     */
    public String compile() {
        compiledContent = new StringBuilder(String.format(TITLE_FORMAT, reportTitle));
        List<String> sorted = testScenarios.stream().sorted().collect(Collectors.toList());
        for (String testScenario : sorted) {
            String[] testParts = testScenario.split("\\|");
            String interSystemRequirement = testParts[INTER_SYSTEM_REQUIREMENT_INDEX];
            boolean isInterSystemRequirementAvailable = !NOT_AVAILABLE.equals(interSystemRequirement);
            if (isInterSystemRequirementAvailable && !reportedInterSystemRequirements.contains
                    (interSystemRequirement)) {
                reportedIntraSystemRequirements.clear();
                compiledContent.append(String.format(INTER_SYSTEM_FORMAT, interSystemRequirement));
                reportedInterSystemRequirements.add(interSystemRequirement);
            }
            String intraSystemRequirement = testParts[INTRA_SYSTEM_REQUIREMENT_INDEX];
            boolean isIntraSystemRequirementAvailable = !NOT_AVAILABLE.equals(intraSystemRequirement);
            if (isIntraSystemRequirementAvailable && !reportedIntraSystemRequirements.contains
                    (intraSystemRequirement)) {
                reportedTests.clear();
                compiledContent.append(String.format(INTRA_SYSTEM_FORMAT, intraSystemRequirement));
                reportedIntraSystemRequirements.add(intraSystemRequirement);
            }
            String test = testParts[TEST_INDEX];
            if (!reportedTests.contains(test)) {
                compiledContent.append(String.format(TEST_FORMAT, test));
                reportedTests.add(test);
            }
            String scenario = testParts[SCENARIO_INDEX];
            boolean isScenarioAvailable = !NOT_AVAILABLE.equals(scenario);
            if (isScenarioAvailable) {
                compiledContent.append(String.format(SCENARIO_FORMAT, scenario));
            }
        }
        return compiledContent.toString();
    }

    /***
     * Add an entry for a test, the scenario in which it was executed, and the requirement covered
     * @param test Name of the test executed
     * @param scenario Description of the data scenario in which the test was executed
     * @param requirement Description of the requirement covered
     */
    public void addEntry(String test, String scenario, String requirement) {
        String validatedRequirement = nullToNa(requirement);
        validatedRequirement = validatedRequirement.contains("|") ? validatedRequirement : String.format("%s|%s",
                NOT_AVAILABLE, validatedRequirement);
        String validatedScenario = nullToNa(scenario);
        String entry = String.format("%s|%s|%s", validatedRequirement, test, validatedScenario);
        if (!testScenarios.contains(entry)) {
            testScenarios.add(entry);
        }
    }

    /***
     * Add entry covering multiple requirements
     * @param test Name of test executed
     * @param scenario Description of the data scenario in which the test was executed
     * @param requirements Comma-delimited list of descriptions of requirements covered
     */
    public void addEntries(String test, String scenario, String requirements) {
        List<String> requirementList = requirements == null ? Collections.singletonList(null) : parseRequirements
                (requirements);
        for (String requirement : requirementList) {
            requirement = requirement == null ? null : requirement.replace(".", "|");
            addEntry(test, scenario, requirement);
        }
        addEntry(test, scenario, null);
    }

    private List<String> parseRequirements(String requirements) {
        List<String> requirementList = extractRequirements(requirements);
        return delimitInterIntraRequirements(requirementList);
    }

    private List<String> delimitInterIntraRequirements(List<String> requirementList) {
        List<String> returnList = new ArrayList<>();
        for (String requirement : requirementList) {
            returnList.add(requirement.replace(".", "|"));
        }
        return returnList;
    }

    private List<String> extractRequirements(String requirements) {
        return requirements == null ? Collections.singletonList(null) : Arrays.stream(requirements.split("\\|")).collect(Collectors.toList());
    }

    private String nullToNa(String scenario) {
        return scenario == null ? String.format("%s", NOT_AVAILABLE) : scenario;
    }

    /***
     * Write the coverage report to file.  Any existing file with the same name will be deleted.
     */
    public void write() {
        deleteReportFile();
        createReportFile();
        writeToReportFile();
    }

    private void writeToReportFile() {
        File file = new File(filename);
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(this.compile());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createReportFile() {
        File file = new File(filename);
        try {
            if (file.createNewFile()) {
                String errorMessage = String.format("Unable to create report file %s", filename);
                LoggerFactory.getLogger(this.getClass()).error(errorMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteReportFile() {
        File file = new File(filename);
        if (file.exists()) {
            if (!file.delete()) {
                String errorMessage = String.format("Unable to delete report file %s", filename);
                LoggerFactory.getLogger(this.getClass()).error(errorMessage);
            }
        }
    }
}
