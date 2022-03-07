# coverage4test
Accepts test result entries and compiles them into JSON formatted System and Requirements coverage reports

## System Coverage
Compilation of every Test and Scenario entry for a given Test Subject, organized by Test Subject, Test and Scenario

Example:

`{"coverage":"system","subjects":[{"subject":"LoginPage","tests":[{"test":"clickLogin","scenarios":[{"scenario":"valid credentials"},{"scenario":"nonexistent user"},{"scenario":"incorrect password"}]},{"test":"clickCancel"}]}]}`

## Requirements Coverage
Compilation of every Requirement referenced Test and Scenario entries for a given Test Subject, organized by Requirement, Subject, Test and Scenario

Example:

`{"coverage":"requirements","requirements":[{"requirement":"UserStory #999","subjects":[{"subject":"LoginPage","tests":[{"test":"clickCancel"}]}]},{"requirement":"UserStory #879","subjects":[{"subject":"LoginPage","tests":[{"test":"clickCancel"}]}]}]}`

# Instantiate new Coverage Report
`CoverageReport report = CoverageReport.getInstance()`

# Add an entry

    report.addTestEntry(test, subject)

    report.addTestEntry(test, subject, data-scenario)

    report.addTestEntry(test, subject, verificationcount, data-scenario, requirement...)

    report.addRequirementTestEntry(test, subject, requirement...)

    report.addRequirementTestEntry(test, subject, data-scenario, requirement...)

# Get System Coverage

    report.getSystemCoverage()

# Get Requirement Coverage
    
    report.getRequirementsCoverage()
    
## License & Copyright

Craig A. Stockton
Licensed under the [Apache 2.0](LICENSE)