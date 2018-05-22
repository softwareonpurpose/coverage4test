package com.softwareonpurpose.traceability4test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class TestSubject {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String description;
    private List<ExecutedTest> test = new ArrayList<>();

    private TestSubject(String description, List<ExecutedTest> tests) {
        this.description = description;
        this.test.addAll(tests);
    }

    TestSubject(String description, ExecutedTest test) {
        this(description, Collections.singletonList(test));
    }

    static TestSubject create(String description, ExecutedTest test) {
        return new TestSubject(description, Collections.singletonList(test));
    }

    static TestSubject create(String description, List<ExecutedTest> tests) {
        return new TestSubject(description, tests);
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
