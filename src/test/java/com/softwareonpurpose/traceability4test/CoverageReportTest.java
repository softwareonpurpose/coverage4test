package com.softwareonpurpose.traceability4test;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class CoverageReportTest {

    @Test
    public void construct_TestOnly_single() {
        CoverageReport.reset();
        String test = "Test.method";
        String expected = String.format("TRACEABILITY REPORT:%n  %s", test);
        CoverageReport coverageReport = CoverageReport.getInstance();
        coverageReport.addEntry(test);
        String actual = coverageReport.construct();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void construct_TestOnly_multiple() {
        CoverageReport.reset();
        String test_1 = "Test.method_1";
        String test_2 = "Test.method_2";
        String expected = String.format("TRACEABILITY REPORT:%n  %s%n  %s", test_1, test_2);
        CoverageReport coverageReport = CoverageReport.getInstance();
        coverageReport.addEntry(test_1);
        coverageReport.addEntry(test_2);
        String actual = coverageReport.construct();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void construct_TestOnly_duplicate() {
        CoverageReport.reset();
        String test_1 = "Test.method_1";
        String expected = String.format("TRACEABILITY REPORT:%n  %s", test_1);
        CoverageReport coverageReport = CoverageReport.getInstance();
        coverageReport.addEntry(test_1);
        coverageReport.addEntry(test_1);
        String actual = coverageReport.construct();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }

    @Test
    public void reset() {
        CoverageReport.reset();
        String expected = "TRACEABILITY REPORT:";
        CoverageReport.getInstance().addEntry("test");
        CoverageReport.reset();
        String actual = CoverageReport.getInstance().construct();
        Assert.assertEquals(actual, expected, "Report failed to resetContent to new content");
    }
}
