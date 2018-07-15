# coverage4test
Provides run-time generated coverage reports for each test class.

## Test Subject Coverage
Every test method executed along with every scenario tested.

## Requirements Coverage
Every requirement verified including every test and scenario by which it was verified.

# Instantiate new Coverage Report
`CoverageReport report = CoverageReport.construct("[test-class]")`

# Add an entry
    report.addEntry(test-method)

    report.addEntry(test-method, data-scenario)

    report.addEntry(test-method, null, requirement...)

    report.addEntry(test-method, data-scenario, requirement...)

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