package com.softwareonpurpose.traceability4test;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

@Test
public class CoverageReportTest {

    @Test
    public void save() {
        String filename = "coverage.rpt";
        CoverageReport.getInstance().write(filename);
        Assert.assertTrue(Files.exists(Paths.get(filename)), "Report file failed to be saved");
    }
}
