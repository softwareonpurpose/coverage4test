package com.softwareonpurpose.traceability4test;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class CoverageReport {
    static StringBuilder content = new StringBuilder("TRACEABILITY REPORT:");
    private final String filename;

    private CoverageReport(String filename) {
        this.filename = filename;
        File file = new File(filename);
        if (file.exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }

    public static CoverageReport getInstance(String filename) {
        return new CoverageReport(filename);
    }

    static String getContent() {
        return content.toString();
    }

    public void write() {
        File file = new File(filename);
        if (!file.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addEntry(String interSystemFeature, String requirement, String verifyingComponent) {
        content.append(String.format("\n%s", interSystemFeature)).append(String.format("\n  %s", requirement)).append
                (String.format("\n    %s", verifyingComponent));
    }
}
