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
        oneTest.addEntry(test_1, subject_1);
        String expectedOneTest_testOnly =
                String.format("{\"coverage\":\"system\", \"subjects\":[{\"subject\":\"%s\", \"tests\":[{\"test\":\"%s\"}]}]}", subject_1, test_1);

        return new Object[][]{
                {oneTest, expectedOneTest_testOnly}
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
        report.addEntry("test 1", "feature 1", 1, "test data 1", "requirement 1", "requirement 2");
        report.addEntry("test 2", "feature 2", 1, "test data 2", "requirement 1", "requirement 2");
        int expected = 2;
        int actual = report.getSystemCoverageCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of requirements");
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
        report.addEntry("test 1", "feature 1", 1, "test data 1", "requirement 1", "requirement 2");
        int expected = 1;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of tests");
    }

    @Test
    public void testAddEntry_testOnlyNullDescription() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry(null, "feature 1", 1, "test data 1", "requirement 1", "requirement 2");
        int expected = 0;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed:  added test with <null> description");
    }

    @Test
    public void testAddEntry_testOnlyEmptyDescription() {
        CoverageReport report = CoverageReport.getInstance();
        report.addEntry("", "feature 1", 1, "test data 1", "requirement 1", "requirement 2");
        int expected = 0;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed:  added test with empty-string description");
    }

    @Test
    public void testAddEntry_testRequirementsEmptyDescription() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("", "feature 1", 1, "test data 1", "");
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

    @Test
    public void testGetSystemCoverage_oneTest_nameSubjectOnly() {
        CoverageReport report = CoverageReport.getInstance();
        String test = "test 1";
        String subject = "subject 1";
        report.addEntry(test, subject);
        String actual = report.getSystemCoverage();
        String expected = String.format("{\"coverage\":\"system\",\"subjects\":[{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"}]}]}", subject, test);
        Assert.assertEquals(actual, expected, "FAiled to return expected report data");
    }

    @Test
    public void testGetSystemCoverage_oneTest_oneScenario() {
        String test = "test";
        String subject = "subject";
        Object scenario = "scenario";
        String expected = String.format("{\"coverage\":\"system\",\"subjects\":[{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\",\"scenarios\":[{\"scenario\":\"%s\"}]}]}]}", subject, test, scenario);
        CoverageReport report = CoverageReport.getInstance();
        report.addEntry(test, subject, scenario);
        String actual = report.getSystemCoverage();
        Assert.assertEquals(actual, expected);
    }
}
