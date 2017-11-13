package com.softwareonpurpose.traceability4test;

import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Test
public class CoverageReportTest {
    private final static String filename = "coverage.rpt";

    @DataProvider
    public static Object[][] scenarios() {
        String scenario_1 = "scenario_1";
        String scenario_2 = "scenario_2";
        List<String> expectedOrder = Arrays.asList(scenario_1, scenario_2);
        return new Object[][]{{scenario_1, scenario_2, expectedOrder}, {scenario_2, scenario_1, expectedOrder}};
    }

    @Test
    public void write_fileCreated() {
        deleteReportFile();
        CoverageReport.getInstance(filename).write();
        Assert.assertTrue(new File(filename).exists(), "Failed to save report file");
    }

    @Test(dependsOnMethods = "write_fileCreated")
    public void write_content() {
        String expected = String.format("%s%n", CoverageReport.reportTitle);
        CoverageReport.getInstance(filename).write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Failed to write content to report file");
    }

    @Test
    public void construct_testOnly_single() {
        String test = "Test";
        String scenario = "scenario";
        String expected = String.format("%s%n%n    %s%n      %s", CoverageReport.reportTitle, test, scenario);
        CoverageReport coverageReport = CoverageReport.getInstance(filename);
        coverageReport.addEntry(test, scenario);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "scenarios")
    public void construct_testOnly_multiple(String scenario_1, String scenario_2, List<String> expectedOrder) {
        String test_1 = "Test";
        String sortedScenario_1 = expectedOrder.get(0);
        String test_2 = "Test";
        String sortedScenario_2 = expectedOrder.get(1);
        String expectedFormat = "%s%n%n    %s%n      %s%n    %s%n      %s";
        String expected = String.format(expectedFormat, CoverageReport.reportTitle, test_1, sortedScenario_1, test_2,
                sortedScenario_2);
        CoverageReport coverageReport = CoverageReport.getInstance(filename);
        coverageReport.addEntry(test_1, scenario_1);
        coverageReport.addEntry(test_2, scenario_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void construct_testOnly_duplicate() {
        String test = "Test";
        String scenario = "scenario";
        String expected = String.format("%s%n%n    %s%n      %s", CoverageReport.reportTitle, test, scenario);
        CoverageReport coverageReport = CoverageReport.getInstance(filename);
        coverageReport.addEntry(test, scenario);
        coverageReport.addEntry(test, scenario);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
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
