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
    private final static String target = "TargetView";
    private final static String filename = String.format("%s.coverage.rpt", target);

    @DataProvider
    public static Object[][] scenarios() {
        String scenario_1 = "scenario_1";
        String scenario_2 = "scenario_2";
        List<String> expectedOrder = Arrays.asList(scenario_1, scenario_2);
        return new Object[][]{{scenario_1, scenario_2, expectedOrder}, {scenario_2, scenario_1, expectedOrder}};
    }

    @DataProvider
    public static Object[][] tests() {
        String test_1 = "Test 1";
        String test_2 = "Test 2";
        List<String> expectedOrder = Arrays.asList(test_1, test_2);
        return new Object[][]{{test_1, test_2, expectedOrder}, {test_2, test_1, expectedOrder}};
    }

    @Test
    public void write_fileCreated() {
        deleteReportFile();
        CoverageReport.getInstance(target).write();
        Assert.assertTrue(new File(filename).exists(), "Failed to save report file");
    }

    @Test(dependsOnMethods = "write_fileCreated")
    public void write_content() {
        String expected = String.format("%s%n", CoverageReport.reportTitle);
        CoverageReport.getInstance(target).write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Failed to write content to report file");
    }

    @Test
    public void construct_test_single() {
        String test = "Test";
        String expected = String.format("%s%n%n    %s", CoverageReport.reportTitle, test);
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test, null, null);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "tests")
    public void construct_test_multiple(String test_1, String test_2, List<String> expectedOrder) {
        String expectedFormat = "%s%n%n    %s%n    %s";
        String expected = String.format(expectedFormat, CoverageReport.reportTitle, expectedOrder.get(0),
                expectedOrder.get(1));
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test_1, null, null);
        coverageReport.addEntry(test_2, null, null);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void construct_test_duplicate() {
        String test = "Test";
        String expected = String.format("%s%n%n    %s", CoverageReport.reportTitle, test);
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test, null, null);
        coverageReport.addEntry(test, null, null);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void construct_testScenario_single() {
        String test = "Test";
        String scenario = "scenario";
        String expected = String.format("%s%n%n    %s%n      %s", CoverageReport.reportTitle, test, scenario);
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test, scenario, null);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "scenarios")
    public void construct_testScenario_multiple(String scenario_1, String scenario_2, List<String> expectedOrder) {
        String test_1 = "Test";
        String test_2 = "Test";
        String expectedFormat = "%s%n%n    %s%n      %s%n      %s";
        String expected = String.format(expectedFormat, CoverageReport.reportTitle, test_1, expectedOrder.get(0), expectedOrder.get(1));
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test_1, scenario_1, null);
        coverageReport.addEntry(test_2, scenario_2, null);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void construct_testScenario_duplicate() {
        String test = "Test";
        String scenario = "scenario";
        String expected = String.format("%s%n%n    %s%n      %s", CoverageReport.reportTitle, test, scenario);
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test, scenario, null);
        coverageReport.addEntry(test, scenario, null);
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
