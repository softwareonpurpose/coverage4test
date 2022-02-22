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

import com.google.gson.Gson;

import java.util.*;

/***
 * CoverageReport accepts entries for each test executed, including each test, scenarios executed,
 * and requirements covered.  Instantiated with the filename to which the compiled report is to be written.
 */
@SuppressWarnings("WeakerAccess")
public class CoverageReport {
    private static final String COVERAGE_ELEMENT_NAME = "coverage";
    private static final String COVERAGE_TYPE_SYSTEM = "system";
    private static final String COVERAGE_TYPE_REQUIREMENTS = "requirements";
    private final SortedSet<ExecutedTest> systemCoverage = new TreeSet<>();
    private final transient Map<String, SortedSet<ExecutedTest>> requirementsCoverage = new HashMap<>();

    private CoverageReport() {
    }

    /***
     * Get an instance of CoverageReport
     * @return CoverageReport instance
     */
    public static CoverageReport getInstance() {
        return new CoverageReport();
    }

    public void addTestEntry(String testName, String testSubject) {
        addTestEntry(testName, testSubject, null, null, (String) null);
    }

    public void addTestEntry(String testName, String feature, Object testData) {
        addTest(testName, feature, null, testData, (String) null);
    }

    public void addTestEntry(String testName, String feature, Integer verificationCount, Object testData, String... requirements) {
        addTest(testName, feature, verificationCount, testData, requirements);
    }

    public void addRequirementTestEntry(String testName, String testSubject, String requirement) {
        addTest(testName, testSubject, null, null, requirement);
    }

    private void addTest(String testName, String feature, Integer verificationCount, Object testData, String... requirements) {
        ExecutedTest test = getTestInstance(testName, feature, verificationCount, testData);
        if (test != null) {
            systemCoverage.add(test);
            if (requirements != null) {
                for (String requirement : requirements) {
                    if (requirementsCoverage.containsKey(requirement)) {
                        requirementsCoverage.get(requirement).add(test);
                    } else {
                        requirementsCoverage.put(requirement, new TreeSet<>(List.of(test)));
                    }
                }
            }
        }
    }

    private ExecutedTest getTestInstance(String testName, String feature, Integer verificationCount, Object testData) {
        Scenario scenario = testData == null ? null : Scenario.getInstance(testData);
        ExecutedTest test = ExecutedTest.getInstance(testName, feature, verificationCount, scenario);
        for (ExecutedTest candidate : systemCoverage) {
            if (candidate.equals(test)) {
                candidate.addScenario(scenario);
                test = candidate;
            }
        }
        return test;
    }

    public int getSystemCoverageCount() {
        return systemCoverage.size();
    }

    public int getTestCount() {
        return systemCoverage.size();
    }

    /***
     * Generate a System Coverage report
     * @return String  JSON formatted report from submitted test execution data
     */
    public String getSystemCoverage() {
        StringBuilder systemCoverageReport =
                new StringBuilder(String.format("{\"%s\":\"%s\"", COVERAGE_ELEMENT_NAME, COVERAGE_TYPE_SYSTEM));
        if (systemCoverage.size() > 0) {
            systemCoverageReport.append(",\"subjects\":[");
            String subject = systemCoverage.first().getSubject();
            systemCoverageReport.append(String.format("{\"subject\":\"%s\",\"tests\":[", subject));
            String testDelimiter = "";
            for (ExecutedTest test : systemCoverage) {
                if (!subject.equals(test.getSubject())) {
                    testDelimiter = "";
                    systemCoverageReport.append("]}");
                    subject = test.getSubject();
                    systemCoverageReport.append(String.format(",{\"subject\":\"%s\",\"tests\":[", subject));
                }
                systemCoverageReport.append(String.format("%s%s", testDelimiter, test));
                testDelimiter = ",";
            }
            systemCoverageReport.append("]}]");
        }
        systemCoverageReport.append("}");
        return systemCoverageReport.toString();
    }

    @Override
    public String toString() {
        return String.format("{\"coverageReport\":%s", new Gson().toJson(systemCoverage));
    }

    public String getRequirementsCoverage() {
        StringBuilder requirementsCoverageReport = new StringBuilder(String.format("{\"%s\":\"%s\"", COVERAGE_ELEMENT_NAME, COVERAGE_TYPE_REQUIREMENTS));
        if (requirementsCoverage.size() > 0) {
            String delimiter = "";
            requirementsCoverageReport.append(",\"requirements\":[");
            for (String key : requirementsCoverage.keySet()) {
                requirementsCoverageReport.append(String.format("%s{\"requirement\":\"%s\",\"subjects\":[", delimiter, key));
                SortedSet<ExecutedTest> executedTests = requirementsCoverage.get(key);
                String subject = executedTests.first().getSubject();
                requirementsCoverageReport.append(String.format("{\"subject\":\"%s\",\"tests\":[", subject));
                String testDelimiter = "";
                for (ExecutedTest test : executedTests) {
                    if (!subject.equals(test.getSubject())) {
                        testDelimiter = "";
                        requirementsCoverageReport.append("]}");
                        subject = test.getSubject();
                        requirementsCoverageReport.append(String.format(",{\"subject\":\"%s\",\"tests\":[", subject));
                    }
                    requirementsCoverageReport.append(String.format("%s%s", testDelimiter, test));
                    testDelimiter = ",";
                }
                requirementsCoverageReport.append("]}]}]");
                delimiter = ",";
            }
        }
        requirementsCoverageReport.append("}");
        return requirementsCoverageReport.toString();
    }
}
