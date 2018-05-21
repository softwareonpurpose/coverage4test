package com.softwareonpurpose.traceability4test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class IntraAppRequirement {
    private final String id;
    private List<ExecutedTest> test = new ArrayList<>();

    IntraAppRequirement(String requirement_id, ExecutedTest any_test) {
        this.id = requirement_id;
        this.test.add(any_test);
    }

    static IntraAppRequirement create(String description, ExecutedTest test) {
        return new IntraAppRequirement(description, test);
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
}
