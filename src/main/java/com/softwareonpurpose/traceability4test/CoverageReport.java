package com.softwareonpurpose.traceability4test;

import com.softwareonpurpose.indentmanager.IndentManager;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class CoverageReport {
    private static CoverageReport instance;
    private final String filename;
    private List<String> requirementVerifications = new ArrayList<>();
    private IndentManager indentManager = IndentManager.getInstance();

    private CoverageReport(String filename) {
        this.filename = filename;
        indentManager.increment();
    }

    public static CoverageReport getInstance(String filename) {
        if (instance == null) {
            instance = new CoverageReport(filename);
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    @Override
    public String toString() {
        StringBuilder content = new StringBuilder("TRACEABILITY REPORT:");
        for (String verification : requirementVerifications) {
            String test = indentManager.format(verification);
            String contentLine = String.format("%n%s", test);
            content.append(contentLine);
        }
        return content.toString();
    }

    public void addEntry(String test) {
        if (!requirementVerifications.contains(test)) {
            requirementVerifications.add(test);
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
            writer.write(this.toString());
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
