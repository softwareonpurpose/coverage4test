# coverage4test
Provides ability to map any tests and data scenario to inter-application and intra-application specifications, and generate an aggregated report.

# Instantiate new Coverage Report
`CoverageReport report = CoverageReport.construct("[tests-subject]")`

# Add an entry
    report.addEntry(tests-subject)

    report.addEntry(tests-subject, data-scenario)

    report.addEntry(tests-subject, null, requirement)

    report.addEntry(tests-subject, data-scenario, requirement)

# Add an entry for multiple requirements
    report.addEntry(tests-subject, data-scenario, requirement...)

# Write report
`report.write()`

# Aggregated reports
Reports are written in the following json format

    {"requirements_coverage":
        [{"id":"requirement id", 
            "subject":
                [{"subject":"subject subject", 
                    "tests":
                        [{"subject":"tests subject", 
                            "scenario":
                                [{"subject":"scenario subject"}]
                         }]
                 }]
         }]
    }