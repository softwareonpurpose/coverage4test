package com.softwareonpurpose.coverage4test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class CoverageReportTests {
    @DataProvider
    public static Object[][] scenarios() {
        String subject_1 = "subject 1";
        String test_1 = "test 1";
        CoverageReport oneTest = CoverageReport.getInstance();
        oneTest.addTestEntry(test_1, subject_1);
        String expectedOneTest_testOnly =
                String.format("{\"coverage\":\"system\", \"subjects\":[{\"subject\":\"%s\", \"tests\":[{\"test\":\"%s\"}]}]}", subject_1, test_1);

        return new Object[][]{
                {oneTest, expectedOneTest_testOnly}
        };
    }

    @DataProvider
    public static Object[][] testOnlyScenarios() {
        String test_1 = "test 1";
        String test_2 = "test 2";
        String subject_1 = "subject 1";
        String subject_2 = "subject 2";
        String expectedSingleTest = String.format("{\"coverage\":\"system\",\"subjects\":[{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"}]}]}", subject_1, test_1);
        CoverageReport singleTest = getInitializedReport(test_1, subject_1);
        String expectedTwoTestsSingleSubject = String.format("{\"coverage\":\"system\",\"subjects\":[{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"},{\"test\":\"%s\"}]}]}", subject_1, test_1, test_2);
        CoverageReport twoTestsSingleSubject = getInitializedReport(test_1, subject_1);
        twoTestsSingleSubject.addTestEntry(test_2, subject_1);
        String expectedTwoTestsTwoSubjects = String.format("{\"coverage\":\"system\",\"subjects\":[{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"}]},{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"}]}]}", subject_1, test_1, subject_2, test_2);
        CoverageReport twoTestsTwoSubjects = getInitializedReport(test_1, subject_1);
        twoTestsTwoSubjects.addTestEntry(test_2, subject_2);
        return new Object[][]{
                {singleTest, expectedSingleTest}
                , {twoTestsSingleSubject, expectedTwoTestsSingleSubject}
                , {twoTestsTwoSubjects, expectedTwoTestsTwoSubjects}
        };
    }

    private static CoverageReport getInitializedReport(String test, String subject) {
        CoverageReport singleTest = CoverageReport.getInstance();
        singleTest.addTestEntry(test, subject);
        return singleTest;
    }

    @DataProvider
    public static Object[][] dataTypeScenarios() {
        return new Object[][]{
                {8L}
                , {"string"}
                , {ExecutedTest.getInstance("testName", "subjectName", 5, Scenario.getInstance("scenario"))}
        };
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testGetInstance() {
        Class expected = CoverageReport.class;
        Class actual = CoverageReport.getInstance().getClass();
        Assert.assertEquals(actual, expected, String.format("Failed to return an instance of %s", expected));
    }

    @Test
    public void testGetRequirementCount_oneTest() {
        CoverageReport report = CoverageReport.getInstance();
        report.addTestEntry("test 1", "feature 1", 1, "test data 1", "requirement 1", "requirement 2");
        report.addTestEntry("test 2", "feature 2", 1, "test data 2", "requirement 1", "requirement 2");
        int expected = 2;
        int actual = report.getSystemCoverageCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of requirements");
    }

    @Test
    public void testGetTestCount_afterInitialization() {
        CoverageReport report = CoverageReport.getInstance();
        int expected = 0;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of tests");
    }

    @Test
    public void testGetTestCount() {
        CoverageReport report = CoverageReport.getInstance();
        report.addTestEntry("test 1", "feature 1", 1, "test data 1", "requirement 1", "requirement 2");
        int expected = 1;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of tests");
    }

    @Test
    public void testAddEntry_nullTestName() {
        CoverageReport report = CoverageReport.getInstance();
        report.addTestEntry(null, "feature 1", 1, "test data 1", "requirement 1", "requirement 2");
        int expected = 0;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed:  added test with <null> description");
    }

    @Test
    public void testAddEntry_testOnlyEmptyDescription() {
        CoverageReport report = CoverageReport.getInstance();
        report.addTestEntry("", "feature 1", 1, "test data 1", "requirement 1", "requirement 2");
        int expected = 0;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed:  added test with empty-string description");
    }

    @Test
    public void testAddEntry_testRequirementsEmptyDescription() {
        CoverageReport report = CoverageReport.getInstance();
        report.addTestEntry("", "feature 1", 1, "test data 1", "");
        int expected = 0;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed:  added test with empty-string description");
    }

    @Test
    public void testGetSystemCoverage_noTests() {
        String expected = "{\"coverage\":\"system\"}";
        String actual = CoverageReport.getInstance().getSystemCoverage();
        Assert.assertEquals(actual, expected, "Failed to return expected report data");
    }

    @Test(dataProvider = "testOnlyScenarios")
    public void testGetSystemCoverage_nameSubjectOnly(CoverageReport report, String expected) {
        String actual = report.getSystemCoverage();
        Assert.assertEquals(actual, expected, "Failed to return expected report data");
    }

    @Test
    public void testGetSystemCoverage_oneTest_oneScenario() {
        String test = "test";
        String subject = "subject";
        Object scenario = "scenario";
        String expected = String.format("{\"coverage\":\"system\",\"subjects\":[{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\",\"scenarios\":[{\"scenario\":\"%s\"}]}]}]}", subject, test, scenario);
        CoverageReport report = CoverageReport.getInstance();
        report.addTestEntry(test, subject, scenario);
        String actual = report.getSystemCoverage();
        Assert.assertEquals(actual, expected);
    }

    @Test(dataProvider = "dataTypeScenarios")
    public void testGetSystemCoverage_oneTestWithOneScenario(Object testData) {
        Object testDataString = testData.getClass().equals(String.class) ? String.format("\"%s\"", testData) : String.format("%s", testData);
        String expected = String.format("{\"coverage\":\"system\",\"subjects\":[{\"subject\":\"feature_1\",\"tests\":[{\"test\":\"test_1\",\"scenarios\":[{\"scenario\":%s}]}]}]}", testDataString);
        CoverageReport report = CoverageReport.getInstance();
        report.addTestEntry("test_1", "feature_1", testData);
        String actual = report.getSystemCoverage();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetSystemCoverage_oneTestMultipleScenarios() {
        String test_1 = "test_1";
        String feature_1 = "feature_1";
        String scenario_1 = "scenario 1";
        String scenario_2 = "scenario 2";
        String expected = String.format("{\"coverage\":\"system\",\"subjects\":[{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\",\"scenarios\":[{\"scenario\":\"%s\"},{\"scenario\":\"%s\"}]}]}]}", feature_1, test_1, scenario_1, scenario_2);
        CoverageReport report = CoverageReport.getInstance();
        report.addTestEntry(test_1, feature_1, scenario_1);
        report.addTestEntry(test_1, feature_1, scenario_2);
        String actual = report.getSystemCoverage();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetSystemCoverage_oneTestDuplicateScenarios() {
        String test_1 = "test_1";
        String feature_1 = "feature_1";
        String scenario_1 = "scenario 1";
        String scenario_2 = "scenario 1";
        String expected = String.format("{\"coverage\":\"system\",\"subjects\":[{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\",\"scenarios\":[{\"scenario\":\"%s\"}]}]}]}", feature_1, test_1, scenario_1);
        CoverageReport report = CoverageReport.getInstance();
        report.addTestEntry(test_1, feature_1, scenario_1);
        report.addTestEntry(test_1, feature_1, scenario_2);
        String actual = report.getSystemCoverage();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetRequirementsCoverage_noTests() {
        String expected = "{\"coverage\":\"requirements\"}";
        CoverageReport report = CoverageReport.getInstance();
        String actual = report.getRequirementsCoverage();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetRequirementsCoverage_oneRequirementOneTest() {
        String requirement = "us-0001";
        String testName = "test 1";
        String testSubject = "feature 1";
        String expected = String.format("{\"coverage\":\"requirements\",\"requirements\":[{\"requirement\":\"%s\",\"subjects\":[{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"}]}]}]}", requirement, testSubject, testName);
        CoverageReport report = CoverageReport.getInstance();
        report.addRequirementTestEntry(testName, testSubject, requirement);
        String actual = report.getRequirementsCoverage();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetRequirementsCoverage_oneRequirementTwoSubjects() {
        String requirement = "us-0001";
        String testName = "test 1";
        String feature_1 = "feature 1";
        String feature_2 = "feature 2";
        String expected = String.format("{\"coverage\":\"requirements\",\"requirements\":[{\"requirement\":\"%s\",\"subjects\":[{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"}]},{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"}]}]}]}", requirement, feature_1, testName, feature_2, testName);
        CoverageReport report = CoverageReport.getInstance();
        report.addRequirementTestEntry(testName, feature_1, requirement);
        report.addRequirementTestEntry(testName, feature_2, requirement);
        String actual = report.getRequirementsCoverage();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetRequirementsCoverage_twoRequirementsOneTest() {
        String requirement_1 = "us-0001";
        String requirement_2 = "us-0002";
        String testName = "test 1";
        String feature_1 = "feature 1";
        String expected = String.format("{\"coverage\":\"requirements\",\"requirements\":[{\"requirement\":\"%s\",\"subjects\":[{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"}]}]},{\"requirement\":\"%s\",\"subjects\":[{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"}]}]}]}", requirement_1, feature_1, testName, requirement_2, feature_1, testName);
        CoverageReport report = CoverageReport.getInstance();
        report.addRequirementTestEntry(testName, feature_1, requirement_1, requirement_2);
        String actual = report.getRequirementsCoverage();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetRequirementsCoverage_nullRequirement(){
        String expected = "{\"coverage\":\"requirements\"}";
        CoverageReport report = CoverageReport.getInstance();
        report.addRequirementTestEntry("test 1","feature 1",(String)null);
        String actual = report.getRequirementsCoverage();
        Assert.assertEquals(actual, expected);
    }

    @Test
    public void testGetRequirementsCoverage_emptyRequirement(){
        String expected = "{\"coverage\":\"requirements\"}";
        CoverageReport report = CoverageReport.getInstance();
        report.addRequirementTestEntry("test 1","feature 1","");
        String actual = report.getRequirementsCoverage();
        Assert.assertEquals(actual, expected);
    }
}
