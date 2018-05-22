package com.softwareonpurpose.traceability4test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;
import java.util.stream.Collectors;

class AppRequirement {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String id;
    private List<ExecutedTest> test = new ArrayList<>();

    private AppRequirement(String requirement_id, List<ExecutedTest> tests) {
        this.id = requirement_id;
        this.test.addAll(tests);
    }

    AppRequirement(String requirementId, ExecutedTest test) {
        this(requirementId, Collections.singletonList(test));
    }

    static AppRequirement create(String description, ExecutedTest test) {
        return new AppRequirement(description, Collections.singletonList(test));
    }

    static AppRequirement create(String description, List<ExecutedTest> tests) {
        return new AppRequirement(description, tests);
    }

    @Override
    public String toString() {
        Collections.sort(test);
        test = test.stream().distinct().collect(Collectors.toList());
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(
                Collection.class, new CollectionSerializer()).create();
        return gson.toJson(this);
    }

    void addTest(String test) {
        this.test.add(ExecutedTest.create(test));
    }

    void addTests(List<ExecutedTest> tests) {
        this.test.addAll(tests);
    }
}
