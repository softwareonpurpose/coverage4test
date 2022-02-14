/*Copyright 2018 Craig A. Stockton

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/
package com.softwareonpurpose.coverage4test;

import java.util.HashMap;
import java.util.Map;

/***
 * CoverageReport accepts entries for each test executed, including each test, scenarios executed,
 * and requirements covered.  Instantiated with the filename to which the compiled report is to be written.
 */
@SuppressWarnings("WeakerAccess")
public class CoverageReport {
    private static final String COVERAGE_ELEMENT_NAME = "coverage";
    private static final String COVERAGE_TYPE_SYSTEM = "system";
    private final String subjectName;
    private final TestedSubject subjectCoverage;
    private final Map<String, SystemRequirement> requirementsCoverage = new HashMap<>();

    private CoverageReport(String subjectName) {
        this.subjectName = subjectName == null ? null : subjectName.replace(" ", "_");
        this.subjectCoverage = TestedSubject.getInstance(this.subjectName);
    }

    /***
     * Get an instance of CoverageReport
     * @param testSubject String description of a test subject
     * @return CoverageReport instance
     */
    public static CoverageReport getInstance(String testSubject) {
        return new CoverageReport(testSubject);
    }

    /***
     * Get an instance of CoverageReport
     * @return CoverageReport instance
     */
    public static CoverageReport getInstance() {
        return new CoverageReport(null);
    }

    /***
     * Add the description of a test
     * @param testDescription String test description
     */
    public void addEntry(String testDescription) {
        if (testDescription == null || testDescription.isEmpty()) {
            return;
        }
        subjectCoverage.merge(TestedSubject.getInstance(subjectName, ExecutedTest.getInstance(testDescription)));
    }

    /***
     * Add the description of a test and any number of requirement IDs
     *
     * @param testDescription String test description
     * @param requirements String... requirement IDs
     */
    public void addEntry(String testDescription, String... requirements) {
        if (testDescription == null || testDescription.isEmpty()) {
            return;
        }
        TestedSubject subject = TestedSubject.getInstance(subjectName, ExecutedTest.getInstance(testDescription));
        subjectCoverage.merge(subject);
        addRequirements(subject, requirements);
    }

    private void addRequirement(String requirement, TestedSubject subject) {
        if (requirement == null || requirement.isEmpty()) {
            return;
        }
        if (requirementsCoverage.containsKey(requirement)) {
            requirementsCoverage.get(requirement).addTestedSubject(subject);
        } else {
            requirementsCoverage.put(requirement, SystemRequirement.getInstance(requirement, subject));
        }
    }

    private void addRequirements(TestedSubject subject, String... requirements) {
        for (String aRequirement : requirements) {
            addRequirement(aRequirement, subject);
        }
    }

    /***
     * Add the description of a test and data scenario (test description is required)
     *
     * @param test String test description
     * @param scenario Scenario initialized with any Object representing a data scenario
     */
    public void addEntry(String test, Scenario scenario) {
        if (test == null || test.isEmpty()) {
            return;
        }
        ExecutedTest executedTest = (scenario == null) ? ExecutedTest.getInstance(test) : ExecutedTest.getInstance(test, scenario);
        subjectCoverage.addTest(executedTest);
    }

    /***
     * Add the description of a test and scenario, and any number of requirement IDs
     *
     * @param test String test description
     * @param scenario Scenario initialized with any Object representing a data scenario
     * @param requirements String... requirement IDs
     */
    public void addEntry(String test, Scenario scenario, String... requirements) {
        if (test == null || test.isEmpty()) {
            return;
        }
        addEntry(test, scenario);
        for (String aRequirement : requirements) {
            if (aRequirement != null && !aRequirement.isEmpty()) {
                addRequirement(aRequirement, TestedSubject.getInstance(subjectName, ExecutedTest.getInstance(test, scenario)));
            }
        }
    }

    public int getRequirementCount() {
        return requirementsCoverage.values().size();
    }

    public int getSubjectCount() {
        return 1;
    }

    public int getTestCount() {
        return subjectCoverage.getTestCount();
    }

    public int getScenarioCount() {
        return subjectCoverage.getScenarioCount();
    }

    /***
     * Generate a System Coverage report
     * @return String  JSON formatted report from submitted test execution data
     */
    public String getSystemCoverage() {
        return String.format("{\"%s\":\"%s\"}", COVERAGE_ELEMENT_NAME, COVERAGE_TYPE_SYSTEM);
    }
}
