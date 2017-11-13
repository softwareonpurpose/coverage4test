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
            indentManager.increment(2);
            String test = indentManager.format(testParts[0]);
            indentManager.increment();
            String scenario = testParts.length > 1 ? indentManager.format(testParts[1]) : "";
            indentManager.decrement();
            indentManager.decrement();
            indentManager.decrement();
            String contentLine = String.format("%n%s%n%s", test, scenario);
            content.append(contentLine);
        }
        return content.toString();
    }

    public void addEntry(String test, String scenario) {
        String entry = String.format("%s|%s", test, scenario);
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
