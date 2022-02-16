package com.softwareonpurpose.coverage4test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class CoverageReportTests {
    @DataProvider
    public static Object[][] scenarios() {
        String testSubject = "testSubject";
        CoverageReport oneSubject = CoverageReport.getInstance(testSubject);
        String oneSubjectExpected =
                String.format("{\"coverage\":\"system\", \"subjects\":[{\"subject\":\"%s\", \"tests\":[]}]}", testSubject);
        return new Object[][]{
                {CoverageReport.getInstance(), "{\"coverage\":\"system\"}"}
                , {oneSubject, oneSubjectExpected}
        };
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testGetInstance() {
        Class expected = CoverageReport.class;
        Class actual = CoverageReport.getInstance("Test Subject").getClass();
        Assert.assertEquals(actual, expected, String.format("Failed to return an instance of %s", expected));
    }

    @Test
    public void testGetRequirementCount_oneTest() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test description", "feature 1", "test data 1", 1, "requirement 1", "requirement 2");
        int expected = 2;
        int actual = report.getRequirementCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of requirements");
    }

    @Test
    public void testGetRequirementCount_twoTestsOneRequirement() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test 1", "feature 1", "test data 1", 1, "requirement 1");
        report.addEntry("test 2", "feature 1", "test data 2", 2, "requirement 1");
        int expected = 1;
        int actual = report.getRequirementCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of requirements");
    }

    @Test
    public void testGetRequirementCount_twoTestsTowRequirements() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test 1", "feature 1", "test data 1", 1, "requirement 1", "requirement 2");
        report.addEntry("test 2", "feature 1", "test data 2", 1, "requirement 1", "requirement 2");
        int expected = 2;
        int actual = report.getRequirementCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of requirements");
    }

    @Test
    public void testGetSubjectCount() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        int expected = 1;
        int actual = report.getSubjectCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of subjects");
    }

    @Test
    public void testGetTestCount_afterInitialization() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        int expected = 0;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of tests");
    }

    @Test
    public void testGetTestCount() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test 1", "feature 1", "test data 1", 1, "requirement 1", "requirement 2");
        int expected = 1;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of tests");
    }

    @Test
    public void testGetScenarioCount() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test 1", "feature 1", "test data 1", 1, "requirement 1", "requirement 2");
        int expected = 1;
        int actual = report.getRecordedExecution();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of scenarios");
    }

    @Test
    public void testAddEntry_testOnlyNullDescription() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry(null, "feature 1", "test data 1", 1, "requirement 1", "requirement 2");
        int expected = 0;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed:  added test with <null> description");
    }

    @Test
    public void testAddEntry_testOnlyEmptyDescription() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("", "feature 1", "test data 1", 1, "requirement 1", "requirement 2");
        int expected = 0;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed:  added test with empty-string description");
    }

    @Test
    public void testAddEntry_testRequirementsNullDescription() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test 1", "feature 1", "test data 1", 1, (String) null);
        int expected = 0;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed:  added test with <null> description");
    }

    @Test
    public void testAddEntry_testRequirementsEmptyDescription() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test 1", "feature 1", "test data 1", 1, "");
        int expected = 0;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed:  added test with empty-string description");
    }

    @Test
    public void testAddRequirement_nullRequirement() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test 1", "feature 1", "test data 1", 1, (String) null);
        int expected = 0;
        int actual = report.getRequirementCount();
        Assert.assertEquals(actual, expected, "Failed:  added requirement with <null> description");
    }

    @Test
    public void testAddRequirement_emptyStringRequirement() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test 1", "feature 1", "test data 1", 1, "");
        int expected = 0;
        int actual = report.getRequirementCount();
        Assert.assertEquals(actual, expected, "Failed:  added requirement with empty-string description");
    }

    @Test
    public void testAddRequirements() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test 1", "feature 1", "test data 1", 1, "requirement 1", "requirement 2");
        int expected = 0;
        int actual = report.getRequirementCount();
        Assert.assertEquals(actual, expected, "Failed:  added requirement with invalid description");
    }

    @Test(dataProvider = "scenarios")
    public void testGetSystemCoverage(CoverageReport report, String expected) {
        String actual = report.getSystemCoverage();
        Assert.assertEquals(actual, expected, "Failed to return expected report data");
    }
}
