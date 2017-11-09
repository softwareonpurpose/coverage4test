package com.softwareonpurpose.traceability4test;

import com.softwareonpurpose.indentmanager.IndentManager;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class CoverageReport {
    private static CoverageReport instance;
    private StringBuilder content = new StringBuilder("TRACEABILITY REPORT:");
    private List<String> requirementVerifications = new ArrayList<>();
    private IndentManager indentManager = IndentManager.getInstance();

    private CoverageReport() {
        indentManager.increment();
    }

    public static CoverageReport getInstance() {
        if (instance == null) {
            instance = new CoverageReport();
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    public String construct() {
        compileVerifications();
        return content.toString();
    }

    void compileVerifications() {
        for (String verification : requirementVerifications) {
            String test = indentManager.format(verification);
            String contentLine = String.format("%n%s", test);
            content.append(contentLine);
        }
    }

    public void addEntry(String test) {
        if (!requirementVerifications.contains(test)) {
            requirementVerifications.add(test);
        }
    }
}
