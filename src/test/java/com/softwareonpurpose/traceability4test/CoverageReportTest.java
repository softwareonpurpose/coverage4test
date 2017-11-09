package com.softwareonpurpose.traceability4test;

import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Test
public class CoverageReportTest {
    private final static String filename = "coverage.rpt";

    @Test
    public void write_fileCreated() {
        CoverageReport.reset();
        deleteReportFile();
        CoverageReport.getInstance(filename).write();
        Assert.assertTrue(new File(filename).exists(), "Failed to save report file");
    }

    @Test(dependsOnMethods = "write_fileCreated")
    public void write_content() {
        String expected = "TRACEABILITY REPORT:";
        CoverageReport.reset();
        CoverageReport.getInstance(filename).write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Failed to write content to report file");
    }

    @Test
    public void construct_testOnly_single() {
        CoverageReport.reset();
        String test = "Test.method";
        String expected = String.format("TRACEABILITY REPORT:%n  %s", test);
        CoverageReport coverageReport = CoverageReport.getInstance(filename);
        coverageReport.addEntry(test);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void construct_testOnly_multiple() {
        CoverageReport.reset();
        String test_1 = "Test.method_1";
        String test_2 = "Test.method_2";
        String expected = String.format("TRACEABILITY REPORT:%n  %s%n  %s", test_1, test_2);
        CoverageReport coverageReport = CoverageReport.getInstance(filename);
        coverageReport.addEntry(test_1);
        coverageReport.addEntry(test_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void construct_testOnly_duplicate() {
        CoverageReport.reset();
        String test_1 = "Test.method_1";
        String expected = String.format("TRACEABILITY REPORT:%n  %s", test_1);
        CoverageReport coverageReport = CoverageReport.getInstance(filename);
        coverageReport.addEntry(test_1);
        coverageReport.addEntry(test_1);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void reset() {
        CoverageReport.reset();
        String expected = "TRACEABILITY REPORT:";
        CoverageReport.getInstance(filename).addEntry("test");
        CoverageReport.reset();
        String actual = CoverageReport.getInstance(filename).toString();
        Assert.assertEquals(actual, expected, "Report failed to resetContent to new content");
    }

    private String readReportFile() {
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(Paths.get(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bytes, StandardCharsets.UTF_8);
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
