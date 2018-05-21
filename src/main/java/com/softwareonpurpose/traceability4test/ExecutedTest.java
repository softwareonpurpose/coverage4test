package com.softwareonpurpose.traceability4test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;

class ExecutedTest {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String description;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final List<DataScenario> scenarios = new ArrayList<>();

    ExecutedTest(String description, String scenarioDescription) {
        this(description, Collections.singletonList(DataScenario.create(scenarioDescription)));
    }

    private ExecutedTest(String testDescription) {
        this(testDescription, new ArrayList<>());
    }

    ExecutedTest(String description, List<DataScenario> scenarios) {
        this.description = description;
        this.scenarios.addAll(scenarios);
    }

    static ExecutedTest create(String description) {
        return new ExecutedTest(description);
    }

    static ExecutedTest create(String description, String scenario) {
        return new ExecutedTest(description, scenario);
    }

    static ExecutedTest create(String description, List<String> scenarios) {
        ExecutedTest test = ExecutedTest.create(description);
        for (String scenario : scenarios) {
            test.addScenario(scenario);
        }
        return test;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(
                Collection.class, new CollectionSerializer()).create();
        return gson.toJson(this);
    }

    void addScenario(String description) {
        scenarios.add(DataScenario.create(description));
    }
}
