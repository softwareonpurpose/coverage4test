package com.softwareonpurpose.traceability4test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class CoverageReport {
    static StringBuilder content = new StringBuilder("TRACEABILITY REPORT:");
    private static CoverageReport instance;
    private final String filename;
    private List<String> requirementVerifications = new ArrayList<>();

    private CoverageReport(String filename) {
        this.filename = filename;
        File file = new File(filename);
        if (file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }

    public static CoverageReport getInstance(String filename) {
        if (instance == null) {
            instance = new CoverageReport(filename);
        }
        return instance;
    }

    static String getContent() {
        return content.toString();
    }

    public void write() {
        compileReport();
        File file = new File(filename);
        createReportFile(file);
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void compileReport() {
        compileVerifications();
    }

    private void compileVerifications() {

    }

    private void createReportFile(File file) {
        if (!file.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addEntry(String interSystemFeature, String requirement, String test) {
        requirementVerifications.add(String.format("%s|%s|%s", interSystemFeature, requirement, test));
    }
}
