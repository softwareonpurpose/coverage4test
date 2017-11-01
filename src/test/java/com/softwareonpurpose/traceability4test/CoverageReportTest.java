package com.softwareonpurpose.traceability4test;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

@Test
public class CoverageReportTest {
    private String filename = "coverage.rpt";

    @Test
    public void save() {

        CoverageReport.getInstance(filename).write();
        Assert.assertTrue(Files.exists(Paths.get(filename)), "Report file failed to be saved");
    }

    @Test
    public void addEntry() {
        String interSystemFeature = "SysId 999";
        String requirement = "User Story 888";
        String verifyingComponent = "Test Component";
        String expected = String.format("TRACEABILITY REPORT:\n%s\n  %s\n    %s", interSystemFeature, requirement,
                verifyingComponent);
        CoverageReport.getInstance(filename).addEntry(interSystemFeature, requirement, verifyingComponent);
        String actual = CoverageReport.getContent();
        Assert.assertEquals(actual, expected, "Report content failed to be compiled correctly");
    }
}
