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
    private List<DataScenario> scenarios = new ArrayList<>();

    ExecutedTest(String description, String scenarioDescription) {
        this(description, Collections.singletonList(DataScenario.create(scenarioDescription)));
    }

    private ExecutedTest(String testDescription) {
        this(testDescription, new ArrayList<>());
    }

    ExecutedTest(String description, Collection<DataScenario> scenarios) {
        this.description = description;
        this.scenarios.addAll(scenarios);
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
        Collections.sort(scenarios);
        scenarios = scenarios.stream().distinct().collect(Collectors.toList());
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(
                Collection.class, new CollectionSerializer()).create();
        return gson.toJson(this);
    }

    void addScenario(String description) {
        scenarios.add(DataScenario.create(description));
    }

    void addScenarios(Collection<String> scenarios) {
        for (String scenario :
                scenarios) {
            this.scenarios.add(DataScenario.create(scenario));
        }
    }
}
