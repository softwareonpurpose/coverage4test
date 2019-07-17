# coverage4test
Accepts test result entries and compiles them into JSON formatted System and Requirements coverage reports

## System Coverage
Compilation of every Test and Scenario entry for a given Test Subject, organized by Test Subject, Test and Scenario

Example:

`{"system_coverage":[{"subject":"LoginPage","tests":[{"test":"clickLogin","scenarios":["valid credentials", "nonexistent user", "incorrect password"]},{"test":"clickCancel"}]}]}`

## Requirements Coverage
Compilation of every Requirement referenced Test and Scenario entries for a given Test Subject, organized by Requirement, Subject, Test and Scenario

Example:

`{"requirements_coverage":[{"id":"UserStory #999","subjects":[{"subject":"LoginPage","tests":[{"test":"clickLogin","scenarios":["valid credentials", "nonexistent user", "incorrect password"]}]}]},{"id":"UserStory #879","subjects":[{"subject":"LoginPage","tests":[{"test":"clickCancel"}]}]}]}`

# Instantiate new Coverage Report
`CoverageReport report = CoverageReport.getInstance("[test-subject]")`

# Add an entry
    report.addEntry(test)

    report.addEntry(test, data-scenario)

    report.addEntry(test, null, requirement...)

    report.addEntry(test, data-scenario, requirement...)

# Write report
`report.write()`

Report files are written to a 'reports' folder off the project root (created if missing).  System and Requirements Coverage report filenames are in the following formats, respectively:

`TestSubject.system.rpt`

`TestSubject.requirements.rpt`.