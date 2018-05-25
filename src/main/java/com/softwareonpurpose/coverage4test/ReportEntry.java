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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

class ReportEntry implements Comparable<ReportEntry> {
    private final String interReq;
    private final String intraReq;
    private final String testName;
    private final String scenario;

    private ReportEntry(String interAppRequirement, String intraAppRequirement, String testName, String scenario) {
        this.interReq = interAppRequirement;
        this.intraReq = intraAppRequirement;
        this.testName = testName;
        this.scenario = scenario;
    }

    static ReportEntry create(String interAppRequirement, String intraAppRequirement, String testName, String scenario) {
        return new ReportEntry(interAppRequirement, intraAppRequirement, testName, scenario);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(interReq).append(intraReq).append(testName).append(scenario).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ReportEntry)) {
            return false;
        }
        ReportEntry comparator = (ReportEntry) obj;
        return new EqualsBuilder()
                .append(interReq, comparator.interReq)
                .append(intraReq, comparator.intraReq)
                .append(testName, comparator.testName)
                .append(scenario, comparator.scenario)
                .isEquals();
    }

    @Override
    public int compareTo(ReportEntry comparator) {
        if (areEqual(this.interReq, comparator.interReq)) {
            if (areEqual(this.intraReq, comparator.intraReq)) {
                if (areEqual(this.testName, comparator.testName)) {
                    return compare(this.scenario, comparator.scenario);
                }
                return compare(this.testName, comparator.testName);
            }
            return compare(this.intraReq, comparator.intraReq);
        }
        return compare(this.interReq, comparator.interReq);
    }

    private int compare(String thisScenario, String compScenario) {
        return thisScenario == null ? -1 : compScenario == null ? 1 : thisScenario.compareTo(compScenario);
    }

    private boolean areEqual(String thisInterReq, String compInterReq) {
        return thisInterReq == null && compInterReq == null || (thisInterReq != null && thisInterReq.equals(compInterReq));
    }

    public String getInterAppRequirement() {
        return interReq;
    }

    public String getIntraAppRequirement() {
        return intraReq;
    }

    public String getTestName() {
        return testName;
    }

    public String getScenario() {
        return scenario;
    }

    public boolean includesRequirement() {
        return intraReq != null;
    }
}
