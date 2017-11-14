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
    private final String filename;
    private List<String> testScenarios = new ArrayList<>();
    private IndentManager indentManager = IndentManager.getInstance();
    private List<String> reportedRequirementTest = new ArrayList<>(

    );

    private CoverageReport(String filename) {
        this.filename = filename;
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
            if (!reportedRequirementTest.contains(testParts[0])) {
                String requirementTest = String.format("%n%s", indentManager.format(testParts[0]));
                content.append(requirementTest);
                reportedRequirementTest.add(testParts[0]);
            }
            if (testParts.length > 1) {
                indentManager.increment();
                String scenario = indentManager.format(testParts[1]);
                content.append(String.format("%n%s", scenario));
                indentManager.decrement();
            }
            indentManager.decrement();
        }
        return content.toString();
    }

    public void addEntry(String test, String scenario) {
        String validatedScenario = scenario == null ? "" : scenario;
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
