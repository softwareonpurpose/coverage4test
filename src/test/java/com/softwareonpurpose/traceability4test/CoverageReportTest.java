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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Test
public class CoverageReportTest {
    private final static String TARGET = "TargetView";
    private final static String FILENAME = String.format("%s.coverage.rpt", TARGET);
    private final static String INTER_APPLICATION_INDENTATION = "    ";
    private final static String INTRA_APPLICATION_INDENTATION = "        ";
    private final static String TEST_INDENTATION = "            ";
    private final static String SCENARIO_INDENTATION = "                ";

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
    public static Object[][] intraAppRequirements() {
        String requirement_1 = "Intra-app Requirement 1";
        String requirement_2 = "Intra-app Requirement 2";
        List<String> expectedOrder = Arrays.asList(requirement_1, requirement_2);
        return new Object[][]{{requirement_1, requirement_2, expectedOrder}, {requirement_2, requirement_1,
                expectedOrder}};
    }

    @DataProvider
    public static Object[][] interAppRequirements() {
        String requirement_1 = "Inter-app Requirement 1";
        String requirement_2 = "Inter-app Requirement 2";
        List<String> expectedOrder = Arrays.asList(requirement_1, requirement_2);
        return new Object[][]{{requirement_1, requirement_2, expectedOrder}, {requirement_2, requirement_1,
                expectedOrder}};
    }

    @DataProvider
    public static Object[][] nullRequirements() {
        String inter_1 = "inter 1";
        String intra_1 = "intra 1";
        String inter_2 = "inter 2";
        String intra_2 = "intra 2";
        return new Object[][]{{null, null, null, intra_1, Arrays.asList(null, null, null, intra_1)}, {null, intra_1,
                null, null, Arrays.asList(null, null, null, intra_1)}, {null, intra_1, null, intra_2, Arrays.asList
                (null, intra_1, null, intra_2)}, {null, intra_2, null, intra_1, Arrays.asList(null, intra_1, null,
                intra_2)}, {null, null, inter_1, intra_1, Arrays.asList(null, null, inter_1, intra_1)}, {inter_1,
                intra_1, null, null, Arrays.asList(null, null, inter_1, intra_1)}, {null, intra_1, inter_2, intra_2,
                Arrays.asList(null, intra_1, inter_2, intra_2)}, {inter_2, intra_2, null, intra_1, Arrays.asList
                (null, intra_1, inter_2, intra_2)}, {null, null, inter_2, intra_2, Arrays.asList(null, null, inter_2,
                intra_2)}, {inter_2, intra_2, null, null, Arrays.asList(null, null, inter_2, intra_2)}, {null,
                intra_2, inter_1, intra_1, Arrays.asList(null, intra_2, inter_1, intra_1)}, {inter_1, intra_1, null,
                intra_2, Arrays.asList(null, intra_2, inter_1, intra_1)}};
    }

    @Test
    public void write_fileCreated() {
        deleteReportFile();
        CoverageReport.getInstance(TARGET).write();
        Assert.assertTrue(new File(FILENAME).exists(), "Failed to save report file");
    }

    @Test(dependsOnMethods = "write_fileCreated")
    public void write_content() {
        String expected = String.format("%s%n", CoverageReport.reportTitle);
        CoverageReport.getInstance(TARGET).write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Failed to write content to report file");
    }

    @Test
    public void test_single() {
        String test = "Test";
        String expected = String.format("%s%n%n%S%s", CoverageReport.reportTitle, TEST_INDENTATION, test);
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntry(test, null, null);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "tests")
    public void test_multiple(String test_1, String test_2, List<String> expectedOrder) {
        String expectedFormat = "%s%n%n%s%s%n%s%s";
        String expected = String.format(expectedFormat, CoverageReport.reportTitle, TEST_INDENTATION, expectedOrder
                .get(0), TEST_INDENTATION, expectedOrder.get(1));
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntry(test_1, null, null);
        coverageReport.addEntry(test_2, null, null);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void test_duplicate() {
        String test = "Test";
        String expected = String.format("%s%n%n%s%s", CoverageReport.reportTitle, TEST_INDENTATION, test);
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntry(test, null, null);
        coverageReport.addEntry(test, null, null);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void testScenario_single() {
        String test = "Test";
        String scenario = "scenario";
        String expected = String.format("%s%n%n%s%s%n%s%s", CoverageReport.reportTitle, TEST_INDENTATION, test,
                SCENARIO_INDENTATION, scenario);
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntry(test, scenario, null);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "scenarios")
    public void testScenario_multiple(String scenario_1, String scenario_2, List<String> expectedOrder) {
        String test_1 = "Test";
        String test_2 = "Test";
        String expectedFormat = "%s%n%n%s%s%n%s%s%n%s%s";
        String expected = String.format(expectedFormat, CoverageReport.reportTitle, TEST_INDENTATION, test_1,
                SCENARIO_INDENTATION, expectedOrder.get(0), SCENARIO_INDENTATION, expectedOrder.get(1));
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntry(test_1, scenario_1, null);
        coverageReport.addEntry(test_2, scenario_2, null);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void testScenario_duplicate() {
        String test = "Test";
        String scenario = "scenario";
        String expected = String.format("%s%n%n%s%s%n%s%s", CoverageReport.reportTitle, TEST_INDENTATION, test,
                SCENARIO_INDENTATION, scenario);
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntry(test, scenario, null);
        coverageReport.addEntry(test, scenario, null);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void intraAppRequirementTest_single() {
        String intraAppRequirement = "Intra-app Requirement";
        String test = "Test";
        String expected = String.format("%s%n%n%s%s%n%s%s", CoverageReport.reportTitle,
                INTRA_APPLICATION_INDENTATION, intraAppRequirement, TEST_INDENTATION, test);
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntry(test, null, intraAppRequirement);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "intraAppRequirements")
    public void intraAppRequirementTest_multipleSorted(String intraAppRequirement_1, String intraAppRequirement_2,
                                                       List<String> expectedOrder) {
        String test = "Test";
        String expectedFormat = "%s%n%n%s%s%n%s%s%n%s%s%n%s%s";
        String expected = String.format(expectedFormat, CoverageReport.reportTitle, INTRA_APPLICATION_INDENTATION,
                expectedOrder.get(0), TEST_INDENTATION, test, INTRA_APPLICATION_INDENTATION, expectedOrder.get(1),
                TEST_INDENTATION, test);
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntry(test, null, intraAppRequirement_1);
        coverageReport.addEntry(test, null, intraAppRequirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void intraAppRequirementTest_multipleSameTest() {
        String intraAppRequirement_1 = "Intra-app Requirement 1";
        String intraAppRequirement_2 = "Intra-app Requirement 2";
        String test = "Test";
        String expectedFormat = "%s%n%n%s%s%n%s%s%n%s%s%n%s%s";
        String expected = String.format(expectedFormat, CoverageReport.reportTitle, INTRA_APPLICATION_INDENTATION,
                intraAppRequirement_1, TEST_INDENTATION, test, INTRA_APPLICATION_INDENTATION, intraAppRequirement_2,
                TEST_INDENTATION, test);
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntry(test, null, intraAppRequirement_1);
        coverageReport.addEntry(test, null, intraAppRequirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void intraAppRequirementTest_duplicate() {
        String intraAppRequirement = "Intra-app Requirement";
        String test = "Test";
        String expected = String.format("%s%n%n%s%s%n%s%s", CoverageReport.reportTitle,
                INTRA_APPLICATION_INDENTATION, intraAppRequirement, TEST_INDENTATION, test);
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntry(test, null, intraAppRequirement);
        coverageReport.addEntry(test, null, intraAppRequirement);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void intraAppRequirementTestScenario_single() {
        String intraAppRequirement = "Intra-app Requirement";
        String test = "Test";
        String scenario = "scenario";
        String expected = String.format("%s%n%n%s%s%n%s%s%n%s%s", CoverageReport.reportTitle,
                INTRA_APPLICATION_INDENTATION, intraAppRequirement, TEST_INDENTATION, test, SCENARIO_INDENTATION,
                scenario);
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntry(test, scenario, intraAppRequirement);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "scenarios")
    public void intraAppRequirementTestScenario_multiple(String scenario_1, String scenario_2, List<String>
            expectedOrder) {
        String intraAppRequirement_1 = "Intra-app Requirement 1";
        String intraAppRequirement_2 = "Intra-app Requirement 2";
        String test_1 = "Test";
        String test_2 = "Test";
        String expectedFormat = "%s%n%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s";
        String expected = String.format(expectedFormat, CoverageReport.reportTitle, INTRA_APPLICATION_INDENTATION,
                intraAppRequirement_1, TEST_INDENTATION, test_1, SCENARIO_INDENTATION, expectedOrder.get(0),
                SCENARIO_INDENTATION, expectedOrder.get(1), INTRA_APPLICATION_INDENTATION, intraAppRequirement_2,
                TEST_INDENTATION, test_1, SCENARIO_INDENTATION, expectedOrder.get(0), SCENARIO_INDENTATION,
                expectedOrder.get(1));
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntry(test_1, scenario_1, intraAppRequirement_1);
        coverageReport.addEntry(test_2, scenario_2, intraAppRequirement_1);
        coverageReport.addEntry(test_1, scenario_1, intraAppRequirement_2);
        coverageReport.addEntry(test_2, scenario_2, intraAppRequirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void intraAppRequirementTestScenario_duplicate() {
        String intraAppRequirement = "Intra-app Requirement";
        String test = "Test";
        String scenario = "scenario";
        String expected = String.format("%s%n%n%s%s%n%s%s%n%s%s", CoverageReport.reportTitle,
                INTRA_APPLICATION_INDENTATION, intraAppRequirement, TEST_INDENTATION, test, SCENARIO_INDENTATION,
                scenario);
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntry(test, scenario, intraAppRequirement);
        coverageReport.addEntry(test, scenario, intraAppRequirement);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "interAppRequirements")
    public void interAppRequirement_multipleSameIntra(String interAppRequirement_1, String interAppRequirement_2,
                                                      List<String> expectedOrder) {
        String intraAppRequirement = "Intra-app Requirement";
        String requirement_1 = composeRequirement(interAppRequirement_1, intraAppRequirement);
        String requirement_2 = composeRequirement(interAppRequirement_2, intraAppRequirement);
        String test = "Test";
        String expectedFormat = "%s%n%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s";
        String expected = String.format(expectedFormat, CoverageReport.reportTitle, INTER_APPLICATION_INDENTATION,
                expectedOrder.get(0), INTRA_APPLICATION_INDENTATION, intraAppRequirement, TEST_INDENTATION, test,
                INTER_APPLICATION_INDENTATION, expectedOrder.get(1), INTRA_APPLICATION_INDENTATION,
                intraAppRequirement, TEST_INDENTATION, test);
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntry(test, null, requirement_1);
        coverageReport.addEntry(test, null, requirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "intraAppRequirements")
    public void intraAppRequirement_multipleIntraSorted(String intraAppRequirement_1, String intraAppRequirement_2,
                                                        List<String> expectedOrder) {
        String interAppRequirement = "Inter-app Requirement";
        String requirement_1 = composeRequirement(interAppRequirement, intraAppRequirement_1);
        String requirement_2 = composeRequirement(interAppRequirement, intraAppRequirement_2);
        String test = "Test";
        String expectedFormat = "%s%n%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s";
        String expected = String.format(expectedFormat, CoverageReport.reportTitle, INTER_APPLICATION_INDENTATION,
                interAppRequirement, INTRA_APPLICATION_INDENTATION, expectedOrder.get(0), TEST_INDENTATION, test,
                INTRA_APPLICATION_INDENTATION, expectedOrder.get(1), TEST_INDENTATION, test);
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntry(test, null, requirement_1);
        coverageReport.addEntry(test, null, requirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void intraAppRequirement_single() {
        String interAppRequirement = "Inter-app Requirement";
        String intraAppRequirement = "Intra-app Requirement";
        String requirement = composeRequirement(interAppRequirement, intraAppRequirement);
        String test = "Test";
        String scenario = "scenario";
        String expected = String.format("%s%n%n%s%s%n%s%s%n%s%s%n%s%s", CoverageReport.reportTitle,
                INTER_APPLICATION_INDENTATION, interAppRequirement, INTRA_APPLICATION_INDENTATION,
                intraAppRequirement, TEST_INDENTATION, test, SCENARIO_INDENTATION, scenario);
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntry(test, scenario, requirement);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "nullRequirements")
    public void nullRequirements_sorting(String interReq_1, String intraReq_1, String interReq_2, String intraReq_2,
                                         List<String> expectedOrder) {
        String requirement_1 = intraReq_1 == null ? null : interReq_1 == null ? intraReq_1 : String.format("%s|%s",
                interReq_1, intraReq_1);
        String requirement_2 = intraReq_2 == null ? null : interReq_2 == null ? intraReq_2 : String.format("%s|%s",
                interReq_2, intraReq_2);
        String test = "Test";
        String expectedInterReq_1 = expectedOrder.get(0) == null ? "" : String.format("%n%s%s",
                INTER_APPLICATION_INDENTATION, expectedOrder.get(0));
        String expectedIntraReq_1 = expectedOrder.get(1) == null ? "" : String.format("%n%s%s",
                INTRA_APPLICATION_INDENTATION, expectedOrder.get(1));
        String expectedInterReq_2 = expectedOrder.get(2) == null ? "" : String.format("%n%s%s",
                INTER_APPLICATION_INDENTATION, expectedOrder.get(2));
        String expectedIntraReq_2 = expectedOrder.get(3) == null ? "" : String.format("%n%s%s",
                INTRA_APPLICATION_INDENTATION, expectedOrder.get(3));
        String expected = String.format("%s%n%s%s%n%sTest%s%s%n%sTest", CoverageReport.reportTitle,
                expectedInterReq_1, expectedIntraReq_1, TEST_INDENTATION, expectedInterReq_2, expectedIntraReq_2,
                TEST_INDENTATION);
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntry(test, null, requirement_1);
        coverageReport.addEntry(test, null, requirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void requirementList() {
        String test = "Test Description";
        String testLine = String.format("%n%s%s", TEST_INDENTATION, test);
        StringBuilder requirementsArgumentBuilder = new StringBuilder();
        StringBuilder loggedRequirementsBuilder = new StringBuilder(String.format("%n%s",
                INTRA_APPLICATION_INDENTATION));
        List<String> requirementsList = new ArrayList<>();
        requirementsList.add("Inter-app.Intra-app");
        for (String requirement : requirementsList) {
            List<String> requirementsPair = Arrays.stream(requirement.split("\\.")).collect(Collectors.toList());
            String inter = requirementsPair.get(0);
            String intra = requirementsPair.get(1);
            inter = inter.equals("") ? "" : String.format("%n%s%s", INTER_APPLICATION_INDENTATION, inter);
            intra = intra.equals("") ? "" : String.format("%n%s%s", INTRA_APPLICATION_INDENTATION, intra);
            requirementsArgumentBuilder.append(inter).append(intra);
            loggedRequirementsBuilder.append(inter).append(intra).append(testLine);
        }
        String expected = String.format("%s%n%s", CoverageReport.reportTitle, loggedRequirementsBuilder.toString());
        CoverageReport coverageReport = CoverageReport.getInstance(TARGET);
        coverageReport.addEntries(test, null, requirementsArgumentBuilder.toString());
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    private String readReportFile() {
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(Paths.get(FILENAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private void deleteReportFile() {
        File file = new File(FILENAME);
        if (file.exists()) {
            if (!file.delete()) {
                String errorMessage = String.format("Unable to delete report file %s", FILENAME);
                LoggerFactory.getLogger(this.getClass()).error(errorMessage);
            }
        }
    }

    private String composeRequirement(String interAppRequirement_1, String intraAppRequirement) {
        return String.format("%s|%s", interAppRequirement_1, intraAppRequirement);
    }
}
