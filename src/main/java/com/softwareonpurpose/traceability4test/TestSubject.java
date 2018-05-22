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
