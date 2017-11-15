package com.softwareonpurpose.traceability4test;

import com.softwareonpurpose.indentmanager.IndentManager;
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
    private final static int INTRA_SYSTEM_REQUIREMENT_INDEX = 0;
    private final static int TEST_INDEX = 1;
    private final static int SCENARIO_INDEX = 2;
    private final static String NEW_LINE = "%n%s";
    private final static String NOT_AVAILABLE = "n/a";
    private final String filename;
    private final String target;
    private List<String> testScenarios = new ArrayList<>();
    private IndentManager indentManager = IndentManager.getInstance();
    private List<String> reportedTests = new ArrayList<>();
    private List<String> reportedIntraSystemRequirements = new ArrayList<>();

    private CoverageReport(String target) {
        this.target = target.replace("Test", "");
        this.filename = String.format("%s.coverage.rpt", this.target);
    }

    public static CoverageReport getInstance(String filename) {
        return new CoverageReport(filename);
    }

    public String compile() {
        StringBuilder content = new StringBuilder(String.format("%s%n", reportTitle));
        List<String> sorted = testScenarios.stream().sorted().collect(Collectors.toList());
        for (String testScenario : sorted) {
            String[] testParts = testScenario.split("\\|");
            indentManager.increment();
            String intraSystemRequirement = testParts[INTRA_SYSTEM_REQUIREMENT_INDEX];
            boolean isIntraSystemRequirementAvailable = !NOT_AVAILABLE.equals(intraSystemRequirement);
            if (isIntraSystemRequirementAvailable && !reportedIntraSystemRequirements.contains(intraSystemRequirement)) {
                reportedTests.clear();
                content.append(formatNewLine(intraSystemRequirement));
                reportedIntraSystemRequirements.add(intraSystemRequirement);
            }
            indentManager.increment();
            String test = testParts[TEST_INDEX];
            if (!reportedTests.contains(test)) {
                String requirementTest = formatNewLine(test);
                content.append(requirementTest);
                reportedTests.add(test);
            }
            String scenario = testParts[SCENARIO_INDEX];
            boolean isScenarioAvailable = !NOT_AVAILABLE.equals(scenario);
            if (isScenarioAvailable) {
                indentManager.increment();
                content.append(formatNewLine(scenario));
                indentManager.decrement();
            }
            indentManager.decrement();
            indentManager.decrement();
        }
        return content.toString();
    }

    private String formatNewLine(String value) {
        return String.format(NEW_LINE, indentManager.format(value));
    }

    public void addEntry(String test, String scenario, String intraSystemRequirement) {
        String validatedIntraSystemRequirement = nullToNa(intraSystemRequirement);
        String validatedScenario = nullToNa(scenario);
        String entry = String.format("%s|%s|%s", validatedIntraSystemRequirement, test, validatedScenario);
        if (!testScenarios.contains(entry)) {
            testScenarios.add(entry);
        }
    }

    private String nullToNa(String scenario) {
        return scenario == null ? NOT_AVAILABLE : scenario;
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
