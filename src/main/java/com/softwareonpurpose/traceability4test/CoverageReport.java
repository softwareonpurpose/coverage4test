package com.softwareonpurpose.traceability4test;

import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private CoverageReport(String target) {
        this.filename = String.format("%s.coverage.rpt", target);
    }

    public static CoverageReport getInstance(String filename) {
        return new CoverageReport(filename);
    }

    public String compile() {
        StringBuilder content = new StringBuilder(String.format(TITLE_FORMAT, reportTitle));
        List<String> sorted = testScenarios.stream().sorted().collect(Collectors.toList());
        for (String testScenario : sorted) {
            String[] testParts = testScenario.split("\\|");
            String interSystemRequirement = testParts[INTER_SYSTEM_REQUIREMENT_INDEX];
            boolean isInterSystemRequirementAvailable = !NOT_AVAILABLE.equals(interSystemRequirement);
            if (isInterSystemRequirementAvailable && !reportedInterSystemRequirements.contains(interSystemRequirement)) {
                reportedIntraSystemRequirements.clear();
                content.append(String.format(INTER_SYSTEM_FORMAT, interSystemRequirement));
                reportedInterSystemRequirements.add(interSystemRequirement);
            }
            String intraSystemRequirement = testParts[INTRA_SYSTEM_REQUIREMENT_INDEX];
            boolean isIntraSystemRequirementAvailable = !NOT_AVAILABLE.equals(intraSystemRequirement);
            if (isIntraSystemRequirementAvailable && !reportedIntraSystemRequirements.contains(intraSystemRequirement)) {
                reportedTests.clear();
                content.append(String.format(INTRA_SYSTEM_FORMAT, intraSystemRequirement));
                reportedIntraSystemRequirements.add(intraSystemRequirement);
            }
            String test = testParts[TEST_INDEX];
            if (!reportedTests.contains(test)) {
                content.append(String.format(TEST_FORMAT, test));
                reportedTests.add(test);
            }
            String scenario = testParts[SCENARIO_INDEX];
            boolean isScenarioAvailable = !NOT_AVAILABLE.equals(scenario);
            if (isScenarioAvailable) {
                content.append(String.format(SCENARIO_FORMAT, scenario));
            }
        }
        return content.toString();
    }

    public void addEntry(String test, String scenario, String requirement) {
        String validatedRequirement = nullToNa(requirement);
        validatedRequirement = validatedRequirement.contains("|") ? validatedRequirement : String.format("%s|%s", NOT_AVAILABLE, validatedRequirement);
        String validatedScenario = nullToNa(scenario);
        String entry = String.format("%s|%s|%s", validatedRequirement, test, validatedScenario);
        if (!testScenarios.contains(entry)) {
            testScenarios.add(entry);
        }
    }

    private String nullToNa(String scenario) {
        return scenario == null ? String.format("%s", NOT_AVAILABLE) : scenario;
    }

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
