# coverage4test
Provides run-time generated coverage reports for each test class.

## Test Subject Coverage
Compilation of every Test and Scenario added as Entry for a given Test Subject.

## Requirements Coverage
Compilation of every Requirement included in a test Entry, along with the related Tests and Scenarios.

# Instantiate new Coverage Report
`CoverageReport report = CoverageReport.construct("[test-class]")`

# Add an entry
    report.addEntry(test)

    report.addEntry(test, data-scenario)

    report.addEntry(test, null, requirement...)

    report.addEntry(test, data-scenario, requirement...)

# Write report
`report.write()`

# Aggregated reports
Reports are written in the following json format

    {"subject_coverage":
        "subjects":
            [{"subject":"description", 
                "tests":
                    [{"test":"description", 
                        "scenarios":
                            [{"scenario"}]
                     }]
             }]
    }

    {"requirements_coverage":
        [{"id":"requirement id", 
            "subjects":
                [{"subject":"description", 
                    "tests":
                        [{"test":"description", 
                            "scenarios":
                                [{"scenario"}]
                         }]
                 }]
         }]
    }