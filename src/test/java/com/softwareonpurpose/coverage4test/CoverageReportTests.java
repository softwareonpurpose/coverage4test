package com.softwareonpurpose.coverage4test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test
public class CoverageReportTests {
    @DataProvider
    public static Object[][] scenarios() {
        return new Object[][]{
                {CoverageReport.getInstance()
                        , "{\"coverage\":\"system\", \"subjects\":[{\"subject\":\"testSubject\"}]}"
                }
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
        report.addEntry("test description", "requirement 1", "requirement 2");
        int expected = 2;
        int actual = report.getRequirementCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of requirements");
    }

    @Test
    public void testGetRequirementCount_twoTestsOneRequirement() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test 1", "requirement 1");
        report.addEntry("test 2", "requirement 1");
        int expected = 1;
        int actual = report.getRequirementCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of requirements");
    }

    @Test
    public void testGetRequirementCount_twoTestsTowRequirements() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test 1", "requirement 1");
        report.addEntry("test 2", "requirement 2");
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
        report.addEntry("test");
        int expected = 1;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of tests");
    }

    @Test
    public void testGetScenarioCount() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test", Scenario.getInstance("scenario"));
        int expected = 1;
        int actual = report.getScenarioCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of scenarios");
    }

    @Test
    public void testAddEntry_testOnlyNullDescription() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry(null);
        int expected = 0;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed:  added test with <null> description");
    }

    @Test
    public void testAddEntry_testOnlyEmptyDescription() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("");
        int expected = 0;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed:  added test with empty-string description");
    }

    @Test
    public void testAddEntry_testRequirementsNullDescription() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry(null, "requirement");
        int expected = 0;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed:  added test with <null> description");
    }

    @Test
    public void testAddEntry_testRequirementsEmptyDescription() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("", "requirement 1", "requirement 2");
        int expected = 0;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed:  added test with empty-string description");
    }

    @Test
    public void testAddRequirement_nullRequirement() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test", (String) null);
        int expected = 0;
        int actual = report.getRequirementCount();
        Assert.assertEquals(actual, expected, "Failed:  added requirement with <null> description");
    }

    @Test
    public void testAddRequirement_emptyStringRequirement() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test", "");
        int expected = 0;
        int actual = report.getRequirementCount();
        Assert.assertEquals(actual, expected, "Failed:  added requirement with empty-string description");
    }

    @Test
    public void testAddRequirements() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test", "", null);
        int expected = 0;
        int actual = report.getRequirementCount();
        Assert.assertEquals(actual, expected, "Failed:  added requirement with invalid description");
    }

    @Test
    public void testAddEntry_nullTestDescriptionWithScenario() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry(null, Scenario.getInstance("scenario"));
        int expected = 0;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed:  added test with <null> test description");
    }

    @Test
    public void testAddEntry_emptyStringTestDescriptionWithScenario() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("", Scenario.getInstance("scenario"));
        int expected = 0;
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed:  added test with <null> test description");
    }

    @Test
    public void testAddEntry_nullScenario() {
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test description", (Scenario) null);
        int expectedTestCount = 1;
        int expectedScenarioCount = 0;
        int actualTestCount = report.getTestCount();
        int actualScenarioCount = report.getScenarioCount();
        Assert.assertEquals(actualTestCount, expectedTestCount, "Failed to add test with <null> scenario");
        Assert.assertEquals(actualScenarioCount, expectedScenarioCount, "Failed:  added <null> scenario");
    }

    @Test
    public void testAddEntry_nullTestDescriptionScenariosRequirements() {
        int expected = 0;
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry(null, Scenario.getInstance("scenario"), "requirement");
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed:  added test with <null> description");
    }

    @Test
    public void testAddEntry_emptyTestDescriptionScenariosRequirements() {
        int expected = 0;
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("", Scenario.getInstance("scenario"), "requirement");
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed:  added test with empty description");
    }

    @Test
    public void testAddEntry_testDescriptionScenariosRequirements() {
        int expected = 1;
        CoverageReport report = CoverageReport.getInstance("Test Subject");
        report.addEntry("test description", Scenario.getInstance("scenario"), "requirement");
        int actual = report.getTestCount();
        Assert.assertEquals(actual, expected, "Failed to add test with scenario and requirements");
    }

    @Test(dataProvider = "scenarios")
    public void testGetSystemCoverage(CoverageReport report, String expected) {
        String actual = report.getSystemCoverage();
        Assert.assertEquals(actual, expected, "Failed to return expected report data");
    }
}
