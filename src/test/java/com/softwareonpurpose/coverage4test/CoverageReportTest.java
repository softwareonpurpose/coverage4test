package com.softwareonpurpose.coverage4test;

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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Test
public class CoverageReportTest {
    private final static String FILENAME_FORMAT = "%s.%s.rpt";
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
                {application, test_1, test_1},
                {requirements, test_1, test_2},
                {requirements, test_2, test_1},
                {requirements, test_2, test_2}
        };
    }

    @DataProvider
    public static Object[][] requirements() {
        String requirement_1 = "requirement 1";
        String requirement_2 = "requirement 2";
        return new Object[][]{{requirement_1, requirement_2}, {requirement_2, requirement_1}, {requirement_1, requirement_1}};
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
                        ? String.format(":[{\"subject\":\"%s\"}]", TEST_SUBJECT) : ""
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

    @Test(dataProvider = "requirements")
    public void singleTestOnlyMultipleRequirements(String requirement_1, String requirement_2) {
        String reportType = "requirements";
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, reportType);
        String testDescription = "a test";
        SubjectCoverage subjectCoverage = SubjectCoverage.construct(TEST_SUBJECT, ExecutedTest.construct(testDescription));
        List<SystemRequirement> requirements = Arrays.asList(
                SystemRequirement.construct(requirement_1, subjectCoverage),
                SystemRequirement.construct(requirement_2, subjectCoverage)
        );
        Collections.sort(requirements);
        requirements = requirements.stream().distinct().collect(Collectors.toList());
        String requirementsCoverage =
                String.format("%s%s", requirements.get(0).toString(),
                        requirements.size() > 1 ? String.format(",%s", requirements.get(1).toString()) : "");
        String expected = String.format("{\"%s\":[%s]}", constructReportTitle(reportType), requirementsCoverage);
        deleteReportFile();
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(testDescription, null, requirement_1, requirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected,
                "'Test' json element with scenario is missing or formatted incorrectly");
    }

    @Test
    public void multipleTestsOneRequirementOnly_requirementsCoverage() {
        String reportType = "requirements";
        reportFile = String.format(FILENAME_FORMAT, TEST_SUBJECT, reportType);
        String test_1 = "first test";
        String test_2 = "second test";
        ExecutedTest executedTest_1 = ExecutedTest.construct(test_1);
        ExecutedTest executedTest_2 = ExecutedTest.construct(test_2);
        SubjectCoverage expectedCoverage = SubjectCoverage.construct(TEST_SUBJECT, executedTest_1);
        expectedCoverage.addTest(executedTest_2);
        String requirement = "requirement";
        SystemRequirement expectedRequirement = SystemRequirement.construct(requirement, expectedCoverage);
        String requirementsCoverage = String.format("%s", expectedRequirement.toString());
        String expected = String.format("{\"%s\":[%s]}", constructReportTitle(reportType), requirementsCoverage);
        deleteReportFile();
        CoverageReport coverageReport = CoverageReport.construct(TEST_SUBJECT);
        coverageReport.addEntry(test_1, null, requirement);
        coverageReport.addEntry(test_2, null, requirement);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "'Test' json element with scenario is missing or formatted incorrectly");
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

    private String constructReportTitle(String reportType) {
        return String.format("%s_coverage", reportType);
    }
}
