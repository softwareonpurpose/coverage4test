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

    @DataProvider
    public static Object[][] intraSystemRequirements() {
        String requirement_1 = "Intra-system Requirement 1";
        String requirement_2 = "Intra-system Requirement 2";
        List<String> expectedOrder = Arrays.asList(requirement_1, requirement_2);
        return new Object[][]{{requirement_1, requirement_2, expectedOrder}, {requirement_2, requirement_1, expectedOrder}};
    }

    @DataProvider
    public static Object[][] interSystemRequirements() {
        String requirement_1 = "Inter-system Requirement 1";
        String requirement_2 = "Inter-system Requirement 2";
        List<String> expectedOrder = Arrays.asList(requirement_1, requirement_2);
        return new Object[][]{{requirement_1, requirement_2, expectedOrder}, {requirement_2, requirement_1, expectedOrder}};
    }

    @DataProvider
    public static Object[][] nullRequirements() {
        String inter_1 = "inter 1";
        String intra_1 = "intra 1";
        String inter_2 = "inter 2";
        String intra_2 = "intra 2";
        return new Object[][]{
                {null, null, null, null, Arrays.asList(null, null, null, null)}
//                , {inter_1, intra_1, inter_2, intra_2, Arrays.asList(inter_1, intra_1, inter_2, intra_2)}
//                , {inter_1, intra_1, inter_2, intra_2, Arrays.asList(inter_1, intra_1, inter_2, intra_2)}
//                , {inter_1, intra_1, inter_2, intra_2, Arrays.asList(inter_1, intra_1, inter_2, intra_2)}
        };
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
        String expected = String.format("%s%n%n      %s", CoverageReport.reportTitle, test);
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test, null, null);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "tests")
    public void construct_test_multiple(String test_1, String test_2, List<String> expectedOrder) {
        String expectedFormat = "%s%n%n      %s%n      %s";
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
        String expected = String.format("%s%n%n      %s", CoverageReport.reportTitle, test);
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
        String expected = String.format("%s%n%n      %s%n        %s", CoverageReport.reportTitle, test, scenario);
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
        String expectedFormat = "%s%n%n      %s%n        %s%n        %s";
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
        String expected = String.format("%s%n%n      %s%n        %s", CoverageReport.reportTitle, test, scenario);
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test, scenario, null);
        coverageReport.addEntry(test, scenario, null);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void construct_intraSystemRequirementTest_single() {
        String intraSystemRequirement = "Intra-system Requirement";
        String test = "Test";
        String expected = String.format("%s%n%n    %s%n      %s", CoverageReport.reportTitle, intraSystemRequirement, test);
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test, null, intraSystemRequirement);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "intraSystemRequirements")
    public void construct_intraSystemRequirementTest_multipleSorted(String intraSystemRequirement_1, String intraSystemRequirement_2, List<String> expectedOrder) {
        String test = "Test";
        String expectedFormat = "%s%n%n    %s%n      %s%n    %s%n      %s";
        String expected = String.format(expectedFormat, CoverageReport.reportTitle, expectedOrder.get(0), test, expectedOrder.get(1), test);
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test, null, intraSystemRequirement_1);
        coverageReport.addEntry(test, null, intraSystemRequirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void construct_intraSystemRequirementTest_multipleSameTest() {
        String intraSystemRequirement_1 = "Intra-system Requirement 1";
        String intraSystemRequirement_2 = "Intra-system Requirement 2";
        String test = "Test";
        String expectedFormat = "%s%n%n    %s%n      %s%n    %s%n      %s";
        String expected = String.format(expectedFormat, CoverageReport.reportTitle, intraSystemRequirement_1, test, intraSystemRequirement_2, test);
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test, null, intraSystemRequirement_1);
        coverageReport.addEntry(test, null, intraSystemRequirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void construct_intraSystemRequirementTest_duplicate() {
        String intraSystemRequirement = "Intra-system Requirement";
        String test = "Test";
        String expected = String.format("%s%n%n    %s%n      %s", CoverageReport.reportTitle, intraSystemRequirement, test);
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test, null, intraSystemRequirement);
        coverageReport.addEntry(test, null, intraSystemRequirement);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void construct_intraSystemRequirementTestScenario_single() {
        String intraSystemRequirement = "Intra-system Requirement";
        String test = "Test";
        String scenario = "scenario";
        String expected = String.format("%s%n%n    %s%n      %s%n        %s", CoverageReport.reportTitle, intraSystemRequirement, test, scenario);
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test, scenario, intraSystemRequirement);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "scenarios")
    public void construct_intraSystemRequirementTestScenario_multiple(String scenario_1, String scenario_2, List<String> expectedOrder) {
        String intraSystemRequirement_1 = "Intra-system Requirement 1";
        String intraSystemRequirement_2 = "Intra-system Requirement 2";
        String test_1 = "Test";
        String test_2 = "Test";
        String expectedFormat = "%s%n%n    %s%n      %s%n        %s%n        %s%n    %s%n      %s%n        %s%n        %s";
        String expected = String.format(expectedFormat, CoverageReport.reportTitle, intraSystemRequirement_1, test_1, expectedOrder.get(0), expectedOrder.get(1), intraSystemRequirement_2, test_1, expectedOrder.get(0), expectedOrder.get(1));
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test_1, scenario_1, intraSystemRequirement_1);
        coverageReport.addEntry(test_2, scenario_2, intraSystemRequirement_1);
        coverageReport.addEntry(test_1, scenario_1, intraSystemRequirement_2);
        coverageReport.addEntry(test_2, scenario_2, intraSystemRequirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void construct_intraSystemRequirementTestScenario_duplicate() {
        String intraSystemRequirement = "Intra-system Requirement";
        String test = "Test";
        String scenario = "scenario";
        String expected = String.format("%s%n%n    %s%n      %s%n        %s", CoverageReport.reportTitle, intraSystemRequirement, test, scenario);
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test, scenario, intraSystemRequirement);
        coverageReport.addEntry(test, scenario, intraSystemRequirement);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "interSystemRequirements")
    public void construct_interInterSystemRequirement_multipleSameIntra(String interSystemRequirement_1, String interSystemRequirement_2, List<String> expectedOrder) {
        String intraSystemRequirement = "Intra-system Requirement";
        String requirement_1 = composeRequirement(interSystemRequirement_1, intraSystemRequirement);
        String requirement_2 = composeRequirement(interSystemRequirement_2, intraSystemRequirement);
        String test = "Test";
        String expectedFormat = "%s%n%n  %s%n    %s%n      %s%n  %s%n    %s%n      %s";
        String expected = String.format(expectedFormat, CoverageReport.reportTitle, expectedOrder.get(0), intraSystemRequirement, test, expectedOrder.get(1), intraSystemRequirement, test);
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test, null, requirement_1);
        coverageReport.addEntry(test, null, requirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "intraSystemRequirements")
    public void construct_interIntraSystemRequirement_multipleIntraSorted(String intraSystemRequirement_1, String intraSystemRequirement_2, List<String> expectedOrder) {
        String interSystemRequirement = "Inter-System Requirement";
        String requirement_1 = composeRequirement(interSystemRequirement, intraSystemRequirement_1);
        String requirement_2 = composeRequirement(interSystemRequirement, intraSystemRequirement_2);
        String test = "Test";
        String expectedFormat = "%s%n%n  %s%n    %s%n      %s%n    %s%n      %s";
        String expected = String.format(expectedFormat, CoverageReport.reportTitle, interSystemRequirement, expectedOrder.get(0), test, expectedOrder.get(1), test);
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test, null, requirement_1);
        coverageReport.addEntry(test, null, requirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void construct_interIntraSystemRequirement_single() {
        String interSystemRequirement = "Inter-System Requirement";
        String intraSystemRequirement = "Intra-system Requirement";
        String requirement = composeRequirement(interSystemRequirement, intraSystemRequirement);
        String test = "Test";
        String scenario = "scenario";
        String expected = String.format("%s%n%n  %s%n    %s%n      %s%n        %s", CoverageReport.reportTitle, interSystemRequirement, intraSystemRequirement, test, scenario);
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test, scenario, requirement);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "nullRequirements")
    public void construct_nullRequirements_sorting(String interReq_1, String intraReq_1, String interReq_2, String intraReq_2, List<String> expectedOrder) {
        String requirement_1 = String.format("%s|%s", interReq_1, intraReq_1);
        String requirement_2 = String.format("%s|%s", interReq_2, intraReq_2);
        String test = "Test";
        String expectedInterReq_1 = expectedOrder.get(0) == null ? "" : String.format("%n  %s", expectedOrder.get(0));
        String expectedIntraReq_1 = expectedOrder.get(1) == null ? "" : String.format("%n    %s", expectedOrder.get(1));
        String expectedInterReq_2 = expectedOrder.get(2) == null ? "" : String.format("%n  %s", expectedOrder.get(2));
        String expectedIntraReq_2 = expectedOrder.get(3) == null ? "" : String.format("%n    %s", expectedOrder.get(3));
        String expected = String.format("%s%n%s%s%n      Test%s%s%n      Test", CoverageReport.reportTitle, expectedInterReq_1, expectedIntraReq_1, expectedInterReq_2, expectedIntraReq_2);
        CoverageReport coverageReport = CoverageReport.getInstance(target);
        coverageReport.addEntry(test, null, requirement_1);
        coverageReport.addEntry(test, null, requirement_2);
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

    private String composeRequirement(String interSystemRequirement_1, String intraSystemRequirement) {
        return String.format("%s|%s", interSystemRequirement_1, intraSystemRequirement);
    }
}
