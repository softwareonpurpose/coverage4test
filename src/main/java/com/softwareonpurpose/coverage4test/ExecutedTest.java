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
import com.google.gson.GsonBuilder;
import com.softwareonpurpose.coverage4test.serializer.MapSerializer;
import com.softwareonpurpose.coverage4test.serializer.SortedSetSerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * Description of an Executed Test including the data scenarios it was executed with
 */
class ExecutedTest implements Comparable<ExecutedTest> {
    private final String test;
    private final String subject;
    private final Long verificationCount;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private SortedSet<Scenario> scenarios = new TreeSet<>();

    private ExecutedTest(String description, Scenario scenario, String subject, Long verificationCount) {
        this.test = description == null ? "[UNDEFINED]" : description;
        this.subject = subject == null ? "[UNDEFINED]" : subject;
        this.verificationCount = verificationCount;
        addScenario(scenario);
    }

    public static ExecutedTest getInstance(String testName, String testSubject, Object testData, long verificationCount) {
        Scenario scenario = Scenario.getInstance(testData);
        return new ExecutedTest(testName, scenario, testSubject, verificationCount);
    }

    /**
     * Add a scenario
     *
     * @param scenario A test Scenario
     */
    void addScenario(Scenario scenario) {
        if (scenario == null) {
            return;
        }
        if (scenarios == null) {
            this.scenarios = new TreeSet<>();
        }
        scenarios.add(scenario);
    }

    /**
     * Add a collection of scenarios
     *
     * @param scenarios Collection of Scenarios
     */
    void addScenarios(Collection<Scenario> scenarios) {
        if (scenarios != null) {
            this.scenarios.addAll(scenarios);
        }
    }

    /**
     * Get the list of test scenarios
     *
     * @return Collection of Scenarios
     */
    Collection<Scenario> getScenarios() {
        return scenarios;
    }

    int getScenarioCount() {
        return scenarios == null ? 0 : scenarios.size();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(test).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }
        ExecutedTest comparator = (ExecutedTest) obj;
        return new EqualsBuilder()
                .append(this.test, comparator.test)
                .append(this.subject, comparator.subject)
                .isEquals();
    }

    @Override
    public int compareTo(ExecutedTest comparator) {
        if (equals(comparator)) {
            return 0;
        }
        return this.subject.equals(comparator.subject)
                ? this.test.compareTo(comparator.test)
                : this.subject.compareTo(comparator.subject);
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Map.class, new MapSerializer())
                .registerTypeHierarchyAdapter(SortedSet.class, new SortedSetSerializer())
                .create();
        return gson.toJson(this);
    }

    String getSubject() {
        return subject;
    }
}
