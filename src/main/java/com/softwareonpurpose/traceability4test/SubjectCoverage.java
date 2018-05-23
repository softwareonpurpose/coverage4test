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

class SubjectCoverage implements Comparable<SubjectCoverage> {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String description;
    private List<ExecutedTest> test = new ArrayList<>();

    private SubjectCoverage(String description, List<ExecutedTest> tests) {
        this.description = description;
        this.test.addAll(tests);
    }

    SubjectCoverage(String description, ExecutedTest test) {
        this(description, Collections.singletonList(test));
    }

    static SubjectCoverage create(String description, ExecutedTest test) {
        return new SubjectCoverage(description, Collections.singletonList(test));
    }

    static SubjectCoverage create(String description, List<ExecutedTest> tests) {
        return new SubjectCoverage(description, tests);
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
        if (!(obj instanceof SubjectCoverage)) {
            return false;
        }
        SubjectCoverage comparator = (SubjectCoverage) obj;
        return new EqualsBuilder().append(this.description, comparator.description).isEquals();
    }

    @Override
    public int compareTo(SubjectCoverage comparator) {
        return this.description == null && comparator.description == null ? 0
                : this.description == null ? -1
                : comparator.description == null ? 1
                : this.description.compareTo(comparator.description);
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
        this.test.add(ExecutedTest.construct(test));
    }

    void addTests(List<ExecutedTest> tests) {
        this.test.addAll(tests);
    }

    List<ExecutedTest> getTests() {
        return test;
    }

    void softCollections() {
        Collections.sort(test);
        for (ExecutedTest aTest : test) {
            aTest.softCollections();
        }
    }
}
