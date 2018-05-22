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
package com.softwareonpurpose.traceability4test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class ExecutedTest implements Comparable<ExecutedTest> {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String description;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private List<DataScenario> scenario = new ArrayList<>();

    ExecutedTest(String description, String scenarioDescription) {
        this(description, Collections.singletonList(DataScenario.create(scenarioDescription)));
    }

    private ExecutedTest(String testDescription) {
        this(testDescription, new ArrayList<>());
    }

    ExecutedTest(String description, Collection<DataScenario> scenarios) {
        this.description = description;
        this.scenario.addAll(scenarios);
    }

    static ExecutedTest create(String description) {
        return new ExecutedTest(description);
    }

    static ExecutedTest create(String description, String scenario) {
        return new ExecutedTest(description, scenario);
    }

    static ExecutedTest create(String description, Collection<String> scenarios) {
        ExecutedTest test = ExecutedTest.create(description);
        for (String scenario : scenarios) {
            test.addScenario(scenario);
        }
        return test;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(description).toHashCode();
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
        return new EqualsBuilder().append(this.description, comparator.description).isEquals();
    }

    @Override
    public int compareTo(ExecutedTest comparator) {
        return this.description == null && comparator.description == null ? 0
                : this.description == null ? -1
                : comparator.description == null ? 1
                : this.description.compareTo(comparator.description);
    }

    @Override
    public String toString() {
        Collections.sort(scenario);
        scenario = scenario.stream().distinct().collect(Collectors.toList());
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(
                Collection.class, new CollectionSerializer()).create();
        return gson.toJson(this);
    }

    void addScenario(String description) {
        scenario.add(DataScenario.create(description));
    }

    void addScenarios(Collection<String> scenarios) {
        for (String scenario :
                scenarios) {
            this.scenario.add(DataScenario.create(scenario));
        }
    }

    void softCollections() {
        Collections.sort(scenario);
    }
}
