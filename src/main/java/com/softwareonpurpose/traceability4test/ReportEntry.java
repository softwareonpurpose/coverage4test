package com.softwareonpurpose.traceability4test;

class ReportEntry {
    private final String interReq;
    private final String intraReq;
    private final String testName;
    private final String scenario;

    private ReportEntry(String interAppRequirement, String intraAppRequirement, String testName, String scenario) {
        this.interReq = interAppRequirement;
        this.intraReq = intraAppRequirement;
        this.testName = testName;
        this.scenario = scenario;
    }

    static ReportEntry create(String interAppRequirement, String intraAppRequirement, String testName, String scenario) {
        return new ReportEntry(interAppRequirement, intraAppRequirement, testName, scenario);
    }
}
