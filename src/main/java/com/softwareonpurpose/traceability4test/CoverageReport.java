package com.softwareonpurpose.traceability4test;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class CoverageReport {
    private final String filename;

    public CoverageReport(String filename) {
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

    public void write() {
        File file = new File(filename);
        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
