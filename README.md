# coverage4test
Provides ability to map any test and data scenario to inter-application and intra-application specifications, and generate an aggregated report.

# Instantiate new Coverage Report
`CoverageReport report = CoverageReport.construct("[test-subject]")`

# Add an entry
    report.addEntry(test-description)

    report.addEntry(test-description, data-scenario)

    report.addEntry(test-description, null, requirement)

    report.addEntry(test-description, data-scenario, requirement)

# Add an entry for multiple requirements
    report.addEntry(test-description, data-scenario, requirement...)

# Write report
`report.write()`

# Aggregated reports
Reports are written in the following json format

    {"requirements_coverage":
        [{"id":"requirement id", 
            "subject":
                [{"description":"subject description", 
                    "test":
                        [{"description":"test description", 
                            "scenario":
                                [{"description":"scenario description"}]
                         }]
                 }]
         }]
    }