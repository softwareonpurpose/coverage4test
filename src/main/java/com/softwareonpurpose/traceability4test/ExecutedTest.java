package com.softwareonpurpose.traceability4test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class ExecutedTest {
    private final String description;
    private final List<DataScenario> scenarios = new ArrayList<>();

    ExecutedTest(String testDescription, String scenarioDescription) {
        description = testDescription;
        if (scenarioDescription != null) {
            scenarios.add(DataScenario.create(scenarioDescription));
        }
    }

    private ExecutedTest(String testDescription) {
        this(testDescription, null);
    }

    static ExecutedTest create(String testDescription) {
        return new ExecutedTest(testDescription);
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(
                Collection.class, new CollectionSerializer()).create();
        return gson.toJson(this);
    }
}
