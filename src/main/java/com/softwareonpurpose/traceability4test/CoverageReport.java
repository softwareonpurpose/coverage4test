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
    private final static String NEW_LINE = "%n%s";
    private final static String NOT_AVAILABLE = "n/a";
    private final String filename;
    private final String target;
    private List<String> testScenarios = new ArrayList<>();
    private IndentManager indentManager = IndentManager.getInstance();
    private List<String> reportedRequirementTests = new ArrayList<>();
    private List<String> reportedTargets = new ArrayList<>();

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
            indentManager.increment();
            if (!reportedTargets.contains(this.target)) {
                content.append(formatNewLine(this.target));
                reportedTargets.add(target);
            }
            String[] testParts = testScenario.split("\\|");
            indentManager.increment();
            if (!reportedRequirementTests.contains(testParts[0])) {
                String requirementTest = formatNewLine(testParts[0]);
                content.append(requirementTest);
                reportedRequirementTests.add(testParts[0]);
            }
            boolean isScenarioAvailable = !NOT_AVAILABLE.equals(testParts[1]);
            if (isScenarioAvailable) {
                indentManager.increment();
                content.append(formatNewLine(testParts[1]));
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

    public void addEntry(String test, String scenario) {
        String validatedScenario = scenario == null ? NOT_AVAILABLE : scenario;
        String entry = String.format("%s|%s", test, validatedScenario);
        if (!testScenarios.contains(entry)) {
            testScenarios.add(entry);
        }
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
