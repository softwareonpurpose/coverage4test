# traceability4test
Provides ability to map any test and data scenario to inter-application and intra-application specifications, and generate an aggregated report.

# Instantiate new Coverage Report
`CoverageReport report = CoverageReport.getInstance("[test-subject]")`

# Add an entry
    report.addEntry(test-description, null, null)

    report.addEntry(test-description, data-scenario, null)

    report.addEntry(test-description, null, requirement)

    report.addEntry(test-description, data-scenario, requirement)

    report.addEntry(test-description, data-scenario, "inter-app-requirement.intra-app-requirement")

# Add entries

    report.addEntry(test-description, null, "requirement, inter-app-requirement.intra-app-requirement, requirement")

# Write report
`report.write()`

# Aggregated reports
The first line of the report will indicate whether it is an 'Application Coverage' report, or a 'Requirements Traceability' report.

All entries contain only test description

    [Filename] Report:

                Test 1 Description
                Test 2 Description

Some entries contain scenarios

    [Filename] Report:

                Test 1 Description
                    Data scenario a
                    Data scenario b
                Test 2 Description

Some entries contain requirements

    [Filename] Report:
    
                Test 1 Description
                    Data scenario a
                    Data scenario b
            Requirement A
                Test 2 Description
        
Some entries contain inter-app and intra-app requirements

    [Filename] Report:
    
                Test 1 Description
                    Data scenario a
                    Data scenario b
            Intra-app Requirement A
                Test 2 Description
        Inter-app Requirement 001
            Intra-app Requirement B
                Test 3 Description