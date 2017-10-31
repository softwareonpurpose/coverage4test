package com.softwareonpurpose.traceability4test;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public class CoverageReport {
    public static CoverageReport getInstance() {
        return new CoverageReport();
    }

    public void write(String filename) {
        File file = new File(filename);
        if(file.exists()){
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
