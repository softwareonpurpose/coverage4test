# coverage4test
Provides ability to map any test and data scenario to inter-application and intra-application specifications, and generate an aggregated report.

# Instantiate new Coverage Report
`CoverageReport report = CoverageReport.construct("[test-subject]")`

# Add an entry
    report.addEntry(test-subject)

    report.addEntry(test-subject, data-scenario)

    report.addEntry(test-subject, null, requirement)

    report.addEntry(test-subject, data-scenario, requirement)

# Add an entry for multiple requirements
    report.addEntry(test-subject, data-scenario, requirement...)

# Write report
`report.write()`

# Aggregated reports
Reports are written in the following json format

    {"requirements_coverage":
        [{"id":"requirement id", 
            "subject":
                [{"subject":"subject subject", 
                    "test":
                        [{"subject":"test subject", 
                            "scenario":
                                [{"subject":"scenario subject"}]
                         }]
                 }]
         }]
    }