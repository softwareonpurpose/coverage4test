package com.softwareonpurpose.coverage4test;

import com.softwareonpurpose.coverage4test.mock.ScenarioObject;
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
    private final static String FILENAME_FORMAT = "./reports/%s.%s.rpt";
    private static final String SCENARIO_DESCRIPTION = "scenario_%s";
    private static final String TEST_DESCRIPTION = "Test %s";
    private static final String REQUIREMENT_DESCRIPTION = "requirement %s";
    private final String TEST_SUBJECT = this.getClass().getSimpleName().replace("Test", "");
    private String reportFile;

    @DataProvider
    public static Object[][] scenarios() {
        ScenarioObject scenario_1 = ScenarioObject.getInstance(String.format(SCENARIO_DESCRIPTION, "1"), null, null);
        ScenarioObject scenario_2 = ScenarioObject.getInstance(String.format(SCENARIO_DESCRIPTION, "2"), null, null);
        return new Object[][]{{scenario_1, scenario_2}, {scenario_2, scenario_1}, {scenario_1, scenario_1}};
    }

    @DataProvider
    public static Object[][] tests() {
        String test_1 = String.format(TEST_DESCRIPTION, "1");
        String test_2 = String.format(TEST_DESCRIPTION, "2");
        String system = "system";
        String requirements = "requirements";
        return new Object[][]{
                {system, test_1, test_2},
                {system, test_2, test_1},
                {system, test_1, test_1},
                {requirements, test_1, test_2}
        };
    }

    @DataProvider
    public static Object[][] requirements() {
        String requirement_1 = String.format(REQUIREMENT_DESCRIPTION, "1");
        String requirement_2 = String.format(REQUIREMENT_DESCRIPTION, "2");
        return new Object[][]{{requirement_1, requirement_2}, {requirement_2, requirement_1}, {requirement_1, requirement_1}};
    }

    @DataProvider
    public static Object[][] reportTypes() {
        return new Object[][]{{"system"}, {"requirements"}};
    }

    @Test()
    public void writeCreatesSystemReportFile() {
        String subject = TEST_SUBJECT + "_01";
        String reportType = "system";
        reportFile = String.format(FILENAME_FORMAT, subject, reportType);
        deleteReportFile();
        CoverageReport.getInstance(subject).write();
        Assert.assertTrue(new File(reportFile).exists(), String.format("Failed to write %s coverage file \"%s\"", reportType, reportFile));
    }

    @Test()
    public void withoutRequirementsWriteCreatesNoRequirementsCoverageFile() {
        String subject = TEST_SUBJECT + "_11";
        String reportType = "requirements";
        reportFile = String.format(FILENAME_FORMAT, subject, reportType);
        CoverageReport.getInstance(subject).write();
        Assert.assertFalse(new File(reportFile).exists(), String.format("Wrote %s coverage file \"%s\" when NO requirements exist", reportType, reportFile));
    }

    @Test()
    public void reportIncludesTitleElement() {
        String reportType = "system";
        String subject = TEST_SUBJECT + "_02";
        String failureMessage = "Failed to write '%s' json element to '%s' report file";
        reportFile = String.format(FILENAME_FORMAT, subject, reportType);
        String expectedTitle = constructReportTitle(reportType);
        String expected = String.format("{\"%s\":%s}", expectedTitle, String.format("[{\"subject\":\"%s\"}]", subject));
        deleteReportFile();
        CoverageReport.getInstance(subject).write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, String.format(failureMessage, expectedTitle, reportType));
    }

    @Test
    public void singleTestOnly_systemCoverage() {
        String subject = TEST_SUBJECT + "_03";
        String reportType = "system";
        String reportTitle = constructReportTitle(reportType);
        String testName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        ExecutedTest executedTest = ExecutedTest.getInstance(testName);
        reportFile = String.format(FILENAME_FORMAT, subject, reportType);
        String expected = String.format("{\"%s\":[%s]}",
                reportTitle, SubjectCoverage.getInstance(subject, executedTest).toString());
        deleteReportFile();
        CoverageReport coverageReport = CoverageReport.getInstance(subject);
        coverageReport.addEntry(testName);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected,
                "'Test Subject' json element with one test is missing from system report or formatted incorrectly");
    }

    @Test
    public void singleTestNullScenarioNullRequirement_systemCoverage() {
        String subject = TEST_SUBJECT + "_04";
        String reportType = "system";
        String reportTitle = constructReportTitle(reportType);
        String testName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        ExecutedTest executedTest = ExecutedTest.getInstance(testName);
        reportFile = String.format(FILENAME_FORMAT, subject, reportType);
        String expected =
                String.format("{\"%s\":[%s]}", reportTitle, SubjectCoverage.getInstance(subject, executedTest).toString());
        deleteReportFile();
        CoverageReport coverageReport = CoverageReport.getInstance(subject);
        coverageReport.addEntry(testName, null, (String[]) null);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected,
                "'Test Subject' json element with one test is missing from system report or formatted incorrectly");
    }

    @Test(dataProvider = "tests")
    public void multipleTestsOnly(String reportType, String test_1, String test_2) {
        String subject = TEST_SUBJECT + "_06";
        String reportTitle = constructReportTitle(reportType);
        reportFile = String.format(FILENAME_FORMAT, subject, reportType);
        List<ExecutedTest> tests = Arrays.asList(ExecutedTest.getInstance(test_1), ExecutedTest.getInstance(test_2));
        String subjectCoverageElement = String.format(":[%s]", SubjectCoverage.getInstance(subject, tests).toString());
        String expected = "system".equals(reportType) ? String.format("{\"%s\"%s}", reportTitle, subjectCoverageElement) : null;
        deleteReportFile();
        CoverageReport coverageReport = CoverageReport.getInstance(subject);
        coverageReport.addEntry(test_1);
        coverageReport.addEntry(test_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected,
                "'Test Subject' json element with two tests is missing from system report or exists for requirements report");
    }

    @Test
    public void singleTestWithScenario_systemCoverage() {
        String subject = TEST_SUBJECT + "_07";
        String reportType = "system";
        reportFile = String.format(FILENAME_FORMAT, subject, reportType);
        ScenarioObject scenario = ScenarioObject.getInstance("a scenario", null, null);
        String testDescription = "a test";
        ExecutedTest test = ExecutedTest.getInstance(testDescription, Scenario.getInstance(scenario));
        SubjectCoverage subjectCoverage = SubjectCoverage.getInstance(subject, test);
        String expected = String.format("{\"%s\":[%s]}", constructReportTitle(reportType), subjectCoverage.toString());
        deleteReportFile();
        CoverageReport coverageReport = CoverageReport.getInstance(subject);
        coverageReport.addEntry(testDescription, scenario);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected,
                "'Test' json element with scenario is missing or formatted incorrectly");
    }

    @Test(dataProvider = "scenarios")
    public void singleTestWithMultipleScenarios_systemCoverage(ScenarioObject scenario_1, ScenarioObject scenario_2) {
        String subject = TEST_SUBJECT + "_08";
        String reportType = "system";
        reportFile = String.format(FILENAME_FORMAT, subject, reportType);
        String testDescription = "a test";
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance(scenario_1), Scenario.getInstance(scenario_2));
        ExecutedTest test = ExecutedTest.getInstance(testDescription, scenarios);
        SubjectCoverage subjectCoverage = SubjectCoverage.getInstance(subject, test);
        String expected = String.format("{\"%s\":[%s]}", constructReportTitle(reportType), subjectCoverage.toString());
        deleteReportFile();
        CoverageReport coverageReport = CoverageReport.getInstance(subject);
        coverageReport.addEntry(testDescription, scenario_1);
        coverageReport.addEntry(testDescription, scenario_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected,
                "'Test' json element with scenario is missing or formatted incorrectly");
    }

    @Test(dataProvider = "requirements")
    public void singleTestOnlyMultipleRequirements(String requirement_1, String requirement_2) {
        String subject = TEST_SUBJECT + "_09";
        String reportType = "requirements";
        reportFile = String.format(FILENAME_FORMAT, subject, reportType);
        String testDescription = "a test";
        SubjectCoverage subjectCoverage = SubjectCoverage.getInstance(subject, ExecutedTest.getInstance(testDescription));
        List<SystemRequirement> requirements = Arrays.asList(
                SystemRequirement.getInstance(requirement_1, subjectCoverage),
                SystemRequirement.getInstance(requirement_2, subjectCoverage)
        );
        Collections.sort(requirements);
        requirements = requirements.stream().distinct().collect(Collectors.toList());
        String requirementsCoverage =
                String.format("%s%s", requirements.get(0).toString(),
                        requirements.size() > 1 ? String.format(",%s", requirements.get(1).toString()) : "");
        String expected = String.format("{\"%s\":[%s]}", constructReportTitle(reportType), requirementsCoverage);
        deleteReportFile();
        CoverageReport coverageReport = CoverageReport.getInstance(subject);
        coverageReport.addEntry(testDescription, null, requirement_1, requirement_2);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected,
                "'Test' json element with scenario is missing or formatted incorrectly");
    }

    @Test
    public void multipleTestsOneRequirementOnly_requirementsCoverage() {
        String subject = TEST_SUBJECT + "_10";
        String reportType = "requirements";
        reportFile = String.format(FILENAME_FORMAT, subject, reportType);
        String test_1 = "first test";
        String test_2 = "second test";
        ExecutedTest executedTest_1 = ExecutedTest.getInstance(test_1);
        ExecutedTest executedTest_2 = ExecutedTest.getInstance(test_2);
        SubjectCoverage expectedCoverage = SubjectCoverage.getInstance(subject, executedTest_1);
        expectedCoverage.addTest(executedTest_2);
        String requirement = "requirement";
        SystemRequirement expectedRequirement = SystemRequirement.getInstance(requirement, expectedCoverage);
        String requirementsCoverage = String.format("%s", expectedRequirement.toString());
        String expected = String.format("{\"%s\":[%s]}", constructReportTitle(reportType), requirementsCoverage);
        deleteReportFile();
        CoverageReport coverageReport = CoverageReport.getInstance(subject);
        coverageReport.addEntry(test_1, null, requirement);
        coverageReport.addEntry(test_2, null, requirement);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected, "'Test' json element with scenario is missing or formatted incorrectly");
    }

    @Test
    public void systemVerificationCount() {
        String subject = TEST_SUBJECT + "_12";
        long verificationCount = 5;
        String reportType = "system";
        String reportTitle = constructReportTitle(reportType);
        String testName = new Object() {
        }.getClass().getEnclosingMethod().getName();
        ExecutedTest executedTest = ExecutedTest.getInstance(testName);
        reportFile = String.format(FILENAME_FORMAT, subject, reportType);
        SubjectCoverage expectedReport = SubjectCoverage.getInstance(subject, executedTest);
        expectedReport.verificationCount(verificationCount);
        String expected = String.format("{\"%s\":[%s]}", reportTitle, expectedReport.toString());
        deleteReportFile();
        CoverageReport coverageReport = CoverageReport.getInstance(subject);
        coverageReport.verificationCount(verificationCount);
        coverageReport.addEntry(testName);
        coverageReport.write();
        String actual = readReportFile();
        Assert.assertEquals(actual, expected,
                "'Test Subject' json element with one test is missing from system report or formatted incorrectly");
    }

    private String readReportFile(String filename) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get(filename));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
