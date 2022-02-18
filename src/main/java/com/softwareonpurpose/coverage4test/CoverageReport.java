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

import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/***
 * CoverageReport accepts entries for each test executed, including each test, scenarios executed,
 * and requirements covered.  Instantiated with the filename to which the compiled report is to be written.
 */
@SuppressWarnings("WeakerAccess")
public class CoverageReport {
    private static final String COVERAGE_ELEMENT_NAME = "coverage";
    private static final String COVERAGE_TYPE_SYSTEM = "system";
    private final SortedSet<ExecutedTest> systemCoverage = new TreeSet<>();
    private final transient Map<String, SortedSet<ExecutedTest>> requirementsCoverage = new HashMap<>();

    private CoverageReport() {
    }

    /***
     * Get an instance of CoverageReport
     * @param testSubject String description of a test subject
     * @return CoverageReport instance
     * @deprecated
     */
    public static CoverageReport getInstance(String testSubject) {
        return new CoverageReport();
    }

    /***
     * Get an instance of CoverageReport
     * @return CoverageReport instance
     */
    public static CoverageReport getInstance() {
        return new CoverageReport();
    }

    public void addEntry(String testName, String testSubject) {
        addEntry(testName, testSubject, null, null, (String) null);
    }

    public void addEntry(String testName, String feature, Object testData) {
        addTest(testName, feature, null, testData, (String) null);
    }

    public void addEntry(String testName, String feature, Integer verificationCount, Object testData, String... requirements) {
        addTest(testName, feature, verificationCount, testData, requirements);
    }

    private void addTest(String testName, String feature, Integer verificationCount, Object testData, String... o) {
        ExecutedTest test = ExecutedTest.getInstance(testName, feature, verificationCount, Scenario.getInstance(testData));
        if (test != null) {
            systemCoverage.add(test);
        }
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
            String delimiter = "";
            for (ExecutedTest test : systemCoverage) {
                if (!subject.equals(test.getSubject())) {
                    delimiter = "";
                    systemCoverageReport.append("]}");
                    subject = test.getSubject();
                    systemCoverageReport.append(String.format(",{\"subject\":\"%s\",\"tests\":[", subject));
                }
                systemCoverageReport.append(String.format("%s%s", delimiter, test));
                delimiter = ",";
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
}
