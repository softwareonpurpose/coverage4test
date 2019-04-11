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

import java.util.*;

/**
 * Description of an Executed Test including the data scenarios it was executed with
 */
class ExecutedTest implements Comparable<ExecutedTest> {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    final String test;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final SortedSet<String> scenarios = new TreeSet<>();

    private ExecutedTest(String description, Collection<String> scenarios) {
        this.test = description;
        this.scenarios.addAll(scenarios);
    }

    /**
     * Get an instance of the described ExecutedTest
     * @param description String description
     * @return ExecutedTest instance
     */
    static ExecutedTest getInstance(String description) {
        return new ExecutedTest(description, new ArrayList<>());
    }

    /**
     * Get an instance of the described ExecutedTest with a scenario
     * @param description String description
     * @param scenario String scenario description
     * @return ExecutedTest instance with a scenario
     */
    static ExecutedTest getInstance(String description, String scenario) {
        return new ExecutedTest(description, Collections.singletonList(scenario));
    }

    /**
     * Get an instance of the described ExecutedTest with a collection of scenarios
     * @param description String description
     * @param scenarios Collection of String scenarios
     * @return ExecutedTest instance with collection of scenarios
     */
    static ExecutedTest getInstance(String description, Collection<String> scenarios) {
        ExecutedTest test = ExecutedTest.getInstance(description);
        for (String scenario : scenarios) {
            test.addScenario(scenario);
        }
        return test;
    }

    /**
     * Add a scenario
     * @param description String description of a scenario
     */
    void addScenario(String description) {
        scenarios.add(description);
    }

    /**
     * Add a collection of scenarios
     * @param scenarios Collection of String scenario descriptions
     */
    void addScenarios(Collection<String> scenarios) {
        this.scenarios.addAll(scenarios);
    }

    /**
     * Get the list of String scenario descriptions
     * @return Collection of Strings
     */
    Collection<String> getScenarios() {
        return scenarios;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(test).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ExecutedTest)) {
            return false;
        }
        ExecutedTest comparator = (ExecutedTest) obj;
        return new EqualsBuilder().append(this.test, comparator.test).isEquals();
    }

    @Override
    public int compareTo(ExecutedTest comparator) {
        return this.test == null && comparator.test == null ? 0
                : this.test == null ? -1
                : comparator.test == null ? 1
                : this.test.compareTo(comparator.test);
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Map.class, new MapSerializer())
                .registerTypeHierarchyAdapter(SortedSet.class, new SortedSetSerializer())
                .create();
        return gson.toJson(this);
    }
}
