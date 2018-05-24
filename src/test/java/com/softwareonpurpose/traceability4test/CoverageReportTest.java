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
    private final static String FILENAME_FORMAT = "%s.%s.rpt";
    private final static String INTER_APPLICATION_INDENTATION = "    ";
    private final static String INTRA_APPLICATION_INDENTATION = "        ";
    private final static String TEST_INDENTATION = "            ";
    private final static String SCENARIO_INDENTATION = "                ";
    private final String TEST_SUBJECT = this.getClass().getSimpleName().replace("Test", "");
    private String reportFile;

    @DataProvider
    public static Object[][] scenarios() {
        String scenario_1 = "scenario_1";
        String scenario_2 = "scenario_2";
        return new Object[][]{{scenario_1, scenario_2}, {scenario_2, scenario_1}, {scenario_1, scenario_1}};
    }

    @DataProvider
    public static Object[][] tests() {
        String test_1 = "Test 1";
        String test_2 = "Test 2";
        String application = "application";
        String requirements = "requirements";
        return new Object[][]{
                {application, test_1, test_2},
                {application, test_2, test_1},
                {requirements, test_1, test_2},
                {application, test_1, test_1}
        };
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

    @DataProvider
    public static Object[][] requirementsLists() {
        String header = CoverageReport.TRACEABILITY_TITLE;
        String test = "Test Description";
        String requirement = "requirement";
        String requirement_1 = "requirement-1";
        String requirement_2 = "requirement-2";
        String inter = "inter-requirement";
        String inter_1 = "inter-requirement-1";
        String inter_2 = "inter-requirement-2";
        String intra = "intra-requirement";
        String intra_1 = "intra-requirement-1";
        String intra_2 = "intra-requirement-2";
        String scenario_1 = String.format("%s, %s.%s", requirement, inter, intra);
        String scenario_2 = String.format("%s, %s", requirement_1, requirement_2);
        String scenario_3 = String.format("%s, %s", requirement_2, requirement_1);
        String scenario_4 = String.format("%s.%s, %s", inter, intra, requirement);
        String scenario_5 = String.format("%s.%s, %s.%s", inter_1, intra_1, inter_2, intra_2);
        String scenario_6 = String.format("%s.%s, %s.%s", inter_1, intra_1, inter_1, intra_2);
        String scenario_7 = String.format("%s.%s, %s.%s", inter_1, intra_2, inter_1, intra_1);
        String scenario_8 = String.format("%s.%s, %s.%s", inter_1, intra_1, inter_2, intra_1);
        String scenario_9 = String.format("%s.%s, %s.%s", inter_2, intra_1, inter_1, intra_1);
        String scenario_10 = String.format(", %s", requirement);
        String scenario_11 = String.format("%s, ", requirement);
        String expected_1 = String.format("%s%n%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s", header,
                INTRA_APPLICATION_INDENTATION, requirement, TEST_INDENTATION, test, INTER_APPLICATION_INDENTATION,
                inter, INTRA_APPLICATION_INDENTATION, intra, TEST_INDENTATION, test);
        String expected_2 = String.format("%s%n%n%s%s%n%s%s%n%s%s%n%s%s", header, INTRA_APPLICATION_INDENTATION,
                requirement_1, TEST_INDENTATION, test, INTRA_APPLICATION_INDENTATION, requirement_2,
                TEST_INDENTATION, test);
        String expected_3 = String.format("%s%n%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s", header,
                INTER_APPLICATION_INDENTATION, inter_1, INTRA_APPLICATION_INDENTATION, intra_1, TEST_INDENTATION,
                test, INTER_APPLICATION_INDENTATION, inter_2, INTRA_APPLICATION_INDENTATION, intra_2,
                TEST_INDENTATION, test);
        String expected_4 = String.format("%s%n%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s", header,
                INTER_APPLICATION_INDENTATION, inter_1, INTRA_APPLICATION_INDENTATION, intra_1, TEST_INDENTATION,
                test, INTRA_APPLICATION_INDENTATION, intra_2, TEST_INDENTATION, test);
        String expected_5 = String.format("%s%n%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s", header,
                INTER_APPLICATION_INDENTATION, inter_1, INTRA_APPLICATION_INDENTATION, intra_1, TEST_INDENTATION,
                test, INTER_APPLICATION_INDENTATION, inter_2, INTRA_APPLICATION_INDENTATION, intra_1,
                TEST_INDENTATION, test);
        String expected_6 = String.format("%s%n%n%s%s%n%s%s", header, INTRA_APPLICATION_INDENTATION, requirement,
                TEST_INDENTATION, test);
        return new Object[][]{{scenario_1, expected_1}, {scenario_2, expected_2}, {scenario_3, expected_2},
                {scenario_4, expected_1}, {scenario_5, expected_3}, {scenario_6, expected_4}, {scenario_7,
                expected_4}, {scenario_8, expected_5}, {scenario_9, expected_5}, {scenario_10, expected_6},
                {scenario_11, expected_6}};
    }

    @DataProvider
    public static Object[][] reportTypes() {
        return new Object[][]{{"application"}, {"requirements"}};
    }

    @Test(dataProvider = "reportTypes")
    public void writeCreatesReportFile(String reportType) {
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, reportType);
        deleteReportFile();
        CoverageReport.construct(TEST_SUBJECT).write();
        Assert.assertTrue(new File(reportFile).exists(), String.format("Failed to write %s coverage file \"%s\"", reportType, reportFile));
    }

    @Test(dataProvider = "reportTypes")
    public void reportIncludesTitleElement(String reportType) {
        String failureMessage = "Failed to write '%s' json element to '%s' report file";
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, reportType);
        String expectedTitle = constructReportTitle(reportType);
        String expected =
                String.format("{\"%s\"%s}", expectedTitle, "application".equals(reportType)
                        ? String.format(":[{\"description\":\"%s\"}]", TEST_SUBJECT) : ""
                );
        deleteReportFile();
        CoverageReport.construct(TEST_SUBJECT).write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, String.format(failureMessage, expectedTitle, reportType));
    }

    @Test
    public void singleTestOnly_applicationCoverage() {
        String reportType = "application";
        String reportTitle = constructReportTitle(reportType);
        String testName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        ExecutedTest executedTest = ExecutedTest.construct(testName);
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, reportType);
        String expected = String.format("{\"%s\":[%s]}",
                reportTitle, SubjectCoverage.construct(TEST_SUBJECT, executedTest).toString());
        deleteReportFile();
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(testName);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected,
                "'Test Subject' json element with one test is missing from application report or formatted incorrectly");
    }

    @Test
    public void singleTestOnly_requirementsCoverage() {
        String reportType = "requirements";
        String reportTitle = constructReportTitle(reportType);
        String testName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, reportType);
        String expected = String.format("{\"%s\"}", reportTitle);
        deleteReportFile();
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(testName);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Coverage content written to requirements report unexpectedly");
    }

    @Test(dataProvider = "tests")
    public void multipleTestsOnly(String reportType, String test_1, String test_2) {
        String reportTitle = constructReportTitle(reportType);
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, reportType);
        List<ExecutedTest> tests = Arrays.asList(ExecutedTest.construct(test_1), ExecutedTest.construct(test_2));
        String subjectCoverageElement = String.format(":[%s]", SubjectCoverage.construct(TEST_SUBJECT, tests).toString());
        String expected =
                String.format("{\"%s\"%s}", reportTitle, "application".equals(reportType) ? subjectCoverageElement : "");
        deleteReportFile();
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(test_1);
        coverageReport.addEntry(test_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected,
                "'Test Subject' json element with two tests is missing from application report or formatted incorrectly");
    }

    @Test
    public void singleTestWithScenario_applicationCoverage() {
        String reportType = "application";
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, reportType);
        String scenarioDescription = "a scenario";
        String testDescription = "a test";
        ExecutedTest test = ExecutedTest.construct(testDescription, scenarioDescription);
        SubjectCoverage subjectCoverage = SubjectCoverage.construct(TEST_SUBJECT, test);
        String expected = String.format("{\"%s\":[%s]}", constructReportTitle(reportType), subjectCoverage.toString());
        deleteReportFile();
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(testDescription, scenarioDescription);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected,
                "'Test' json element with scenario is missing or formatted incorrectly");
    }

    @Test(dataProvider = "scenarios")
    public void singleTestWithMultipleScenarios_applicationCoverage(String scenario_1, String scenario_2) {
        String reportType = "application";
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, reportType);
        String testDescription = "a test";
        List<String> scenarios = Arrays.asList(scenario_1, scenario_2);
        ExecutedTest test = ExecutedTest.construct(testDescription, scenarios);
        SubjectCoverage subjectCoverage = SubjectCoverage.construct(TEST_SUBJECT, test);
        String expected = String.format("{\"%s\":[%s]}", constructReportTitle(reportType), subjectCoverage.toString());
        deleteReportFile();
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(testDescription, scenario_1);
        coverageReport.addEntry(testDescription, scenario_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected,
                "'Test' json element with scenario is missing or formatted incorrectly");
    }

    @Test
    public void testScenario_duplicate() {
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, "coverage");
        deleteReportFile();
        String testName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String scenario = "scenario";
        String expected = String.format("%s%n%n%s%s%n%s%s", CoverageReport.COVERAGE_TITLE, TEST_INDENTATION, testName,
                SCENARIO_INDENTATION, scenario);
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(testName, scenario, null);
        coverageReport.addEntry(testName, scenario, null);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void testSubjectTest_single() {
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, "traceability");
        deleteReportFile();
        String testSubject = "Intra-app Requirement";
        String testName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String expected = String.format("%s%n%n%s%s%n%s%s", CoverageReport.TRACEABILITY_TITLE,
                INTRA_APPLICATION_INDENTATION, testSubject, TEST_INDENTATION, testName);
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(testName, null, testSubject);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "intraAppRequirements")
    public void intraAppRequirementTest_multipleSorted(String intraAppRequirement_1, String intraAppRequirement_2,
                                                       List<String> expectedOrder) {
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, "traceability");
        deleteReportFile();
        String testName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String expectedFormat = "%s%n%n%s%s%n%s%s%n%s%s%n%s%s";
        String expected = String.format(expectedFormat, CoverageReport.TRACEABILITY_TITLE, INTRA_APPLICATION_INDENTATION,
                expectedOrder.get(0), TEST_INDENTATION, testName, INTRA_APPLICATION_INDENTATION, expectedOrder.get(1),
                TEST_INDENTATION, testName);
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(testName, null, intraAppRequirement_1);
        coverageReport.addEntry(testName, null, intraAppRequirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void intraAppRequirementTest_multipleSameTest() {
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, "traceability");
        deleteReportFile();
        String intraAppRequirement_1 = "Intra-app Requirement 1";
        String intraAppRequirement_2 = "Intra-app Requirement 2";
        String testName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String expectedFormat = "%s%n%n%s%s%n%s%s%n%s%s%n%s%s";
        String expected = String.format(expectedFormat, CoverageReport.TRACEABILITY_TITLE, INTRA_APPLICATION_INDENTATION,
                intraAppRequirement_1, TEST_INDENTATION, testName, INTRA_APPLICATION_INDENTATION, intraAppRequirement_2,
                TEST_INDENTATION, testName);
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(testName, null, intraAppRequirement_1);
        coverageReport.addEntry(testName, null, intraAppRequirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void intraAppRequirementTest_duplicate() {
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, "traceability");
        deleteReportFile();
        String intraAppRequirement = "Intra-app Requirement";
        String testName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String expected = String.format("%s%n%n%s%s%n%s%s", CoverageReport.TRACEABILITY_TITLE,
                INTRA_APPLICATION_INDENTATION, intraAppRequirement, TEST_INDENTATION, testName);
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(testName, null, intraAppRequirement);
        coverageReport.addEntry(testName, null, intraAppRequirement);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void intraAppRequirementTestScenario_single() {
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, "traceability");
        deleteReportFile();
        String intraAppRequirement = "Intra-app Requirement";
        String testName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String scenario = "scenario";
        String expected = String.format("%s%n%n%s%s%n%s%s%n%s%s", CoverageReport.TRACEABILITY_TITLE,
                INTRA_APPLICATION_INDENTATION, intraAppRequirement, TEST_INDENTATION, testName, SCENARIO_INDENTATION,
                scenario);
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(testName, scenario, intraAppRequirement);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "scenarios", enabled = false)
    public void intraAppRequirementTestScenario_multiple(String scenario_1, String scenario_2) {
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, "traceability");
        deleteReportFile();
        String intraAppRequirement_1 = "Intra-app Requirement 1";
        String intraAppRequirement_2 = "Intra-app Requirement 2";
        String testName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String expectedFormat = "%s%n%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s";
        String expected = String.format(expectedFormat, CoverageReport.TRACEABILITY_TITLE, INTRA_APPLICATION_INDENTATION,
                intraAppRequirement_1, TEST_INDENTATION, testName, SCENARIO_INDENTATION, "",
                SCENARIO_INDENTATION, "", INTRA_APPLICATION_INDENTATION, intraAppRequirement_2,
                TEST_INDENTATION, testName, SCENARIO_INDENTATION, "", SCENARIO_INDENTATION,
                "");
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(testName, scenario_1, intraAppRequirement_1);
        coverageReport.addEntry(testName, scenario_2, intraAppRequirement_1);
        coverageReport.addEntry(testName, scenario_1, intraAppRequirement_2);
        coverageReport.addEntry(testName, scenario_2, intraAppRequirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void intraAppRequirementTestScenario_duplicate() {
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, "traceability");
        deleteReportFile();
        String intraAppRequirement = "Intra-app Requirement";
        String testName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String scenario = "scenario";
        String expected = String.format("%s%n%n%s%s%n%s%s%n%s%s", CoverageReport.TRACEABILITY_TITLE,
                INTRA_APPLICATION_INDENTATION, intraAppRequirement, TEST_INDENTATION, testName, SCENARIO_INDENTATION,
                scenario);
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(testName, scenario, intraAppRequirement);
        coverageReport.addEntry(testName, scenario, intraAppRequirement);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "interAppRequirements")
    public void interAppRequirement_multipleSameIntra(String interAppRequirement_1, String interAppRequirement_2,
                                                      List<String> expectedOrder) {
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, "traceability");
        deleteReportFile();
        String intraAppRequirement = "Intra-app Requirement";
        String requirement_1 = composeRequirement(interAppRequirement_1, intraAppRequirement);
        String requirement_2 = composeRequirement(interAppRequirement_2, intraAppRequirement);
        String testName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String expectedFormat = "%s%n%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s";
        String expected = String.format(expectedFormat, CoverageReport.TRACEABILITY_TITLE, INTER_APPLICATION_INDENTATION,
                expectedOrder.get(0), INTRA_APPLICATION_INDENTATION, intraAppRequirement, TEST_INDENTATION, testName,
                INTER_APPLICATION_INDENTATION, expectedOrder.get(1), INTRA_APPLICATION_INDENTATION,
                intraAppRequirement, TEST_INDENTATION, testName);
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(testName, null, requirement_1);
        coverageReport.addEntry(testName, null, requirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "intraAppRequirements")
    public void intraAppRequirement_multipleIntraSorted(String intraAppRequirement_1, String intraAppRequirement_2,
                                                        List<String> expectedOrder) {
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, "traceability");
        deleteReportFile();
        String interAppRequirement = "Inter-app Requirement";
        String requirement_1 = composeRequirement(interAppRequirement, intraAppRequirement_1);
        String requirement_2 = composeRequirement(interAppRequirement, intraAppRequirement_2);
        String testName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String expectedFormat = "%s%n%n%s%s%n%s%s%n%s%s%n%s%s%n%s%s";
        String expected = String.format(expectedFormat, CoverageReport.TRACEABILITY_TITLE, INTER_APPLICATION_INDENTATION,
                interAppRequirement, INTRA_APPLICATION_INDENTATION, expectedOrder.get(0), TEST_INDENTATION, testName,
                INTRA_APPLICATION_INDENTATION, expectedOrder.get(1), TEST_INDENTATION, testName);
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(testName, null, requirement_1);
        coverageReport.addEntry(testName, null, requirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void intraAppRequirement_single() {
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, "traceability");
        deleteReportFile();
        String interAppRequirement = "Inter-app Requirement";
        String intraAppRequirement = "Intra-app Requirement";
        String requirement = composeRequirement(interAppRequirement, intraAppRequirement);
        String testName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String scenario = "scenario";
        String expected = String.format("%s%n%n%s%s%n%s%s%n%s%s%n%s%s", CoverageReport.TRACEABILITY_TITLE,
                INTER_APPLICATION_INDENTATION, interAppRequirement, INTRA_APPLICATION_INDENTATION,
                intraAppRequirement, TEST_INDENTATION, testName, SCENARIO_INDENTATION, scenario);
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(testName, scenario, requirement);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "nullRequirements")
    public void nullRequirements_sorting(String interReq_1, String intraReq_1, String interReq_2, String intraReq_2,
                                         List<String> expectedOrder) {
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, "traceability");
        deleteReportFile();
        String requirement_1 = intraReq_1 == null ? null : interReq_1 == null ? intraReq_1 : String.format("%s.%s",
                interReq_1, intraReq_1);
        String requirement_2 = intraReq_2 == null ? null : interReq_2 == null ? intraReq_2 : String.format("%s.%s",
                interReq_2, intraReq_2);
        String testName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        String expectedInterReq_1 = expectedOrder.get(0) == null ? "" : String.format("%n%s%s",
                INTER_APPLICATION_INDENTATION, expectedOrder.get(0));
        String expectedIntraReq_1 = expectedOrder.get(1) == null ? "" : String.format("%n%s%s",
                INTRA_APPLICATION_INDENTATION, expectedOrder.get(1));
        String expectedInterReq_2 = expectedOrder.get(2) == null ? "" : String.format("%n%s%s",
                INTER_APPLICATION_INDENTATION, expectedOrder.get(2));
        String expectedIntraReq_2 = expectedOrder.get(3) == null ? "" : String.format("%n%s%s",
                INTRA_APPLICATION_INDENTATION, expectedOrder.get(3));
        String expected = String.format("%s%n%s%s%n%s%s%s%s%n%s%s", CoverageReport.TRACEABILITY_TITLE,
                expectedInterReq_1, expectedIntraReq_1, TEST_INDENTATION, testName, expectedInterReq_2, expectedIntraReq_2,
                TEST_INDENTATION, testName);
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(testName, null, requirement_1);
        coverageReport.addEntry(testName, null, requirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test(dataProvider = "requirementsLists")
    public void requirementList(String requirements, String expected) {
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, "traceability");
        deleteReportFile();
        String testName = "Test Description";
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntries(testName, null, requirements);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    private String readReportFile(String filename) {
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(Paths.get(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private String readReportFile() {
        return readReportFile(reportFile);
    }

    private void deleteReportFile() {
        File file = new File(reportFile);
        if (file.exists()) {
            if (!file.delete()) {
                String errorMessage = String.format("Unable to delete report file %s", reportFile);
                LoggerFactory.getLogger(this.getClass()).error(errorMessage);
            }
        }
    }

    private String composeRequirement(String interAppRequirement_1, String intraAppRequirement) {
        return String.format("%s.%s", interAppRequirement_1, intraAppRequirement);
    }

    private String constructReportTitle(String reportType) {
        return String.format("%s_coverage", reportType);
    }
}
