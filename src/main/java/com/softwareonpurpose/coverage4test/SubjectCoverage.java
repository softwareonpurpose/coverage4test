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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;

class SubjectCoverage implements Comparable<SubjectCoverage> {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String subject;
    private SortedSet<ExecutedTest> test = new TreeSet<>();

    private SubjectCoverage(String subject, List<ExecutedTest> tests) {
        this.subject = subject;
        this.test.addAll(tests);
    }

    SubjectCoverage(String subject, ExecutedTest test) {
        this(subject, Collections.singletonList(test));
    }

    static SubjectCoverage construct(String description, ExecutedTest test) {
        return new SubjectCoverage(description, Collections.singletonList(test));
    }

    static SubjectCoverage construct(String description, List<ExecutedTest> tests) {
        return new SubjectCoverage(description, tests);
    }

    static SubjectCoverage construct(String reportSubject) {
        return new SubjectCoverage(reportSubject, new ArrayList<>());
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(subject).toHashCode();
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
        return new EqualsBuilder().append(this.subject, comparator.subject).isEquals();
    }

    @Override
    public int compareTo(SubjectCoverage comparator) {
        return this.subject == null && comparator.subject == null ? 0
                : this.subject == null ? -1
                : comparator.subject == null ? 1
                : this.subject.compareTo(comparator.subject);
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(
                Collection.class, new CollectionSerializer()).create();
        return gson.toJson(this);
    }

    void addTest(ExecutedTest test) {
        if (this.test.contains(test)) {
            ArrayList<ExecutedTest> tests = new ArrayList<>(this.test);
            int index = tests.indexOf(test);
            test.merge(tests.get(index));
            this.test.remove(test);
        }
        this.test.add(test);
    }

    void addTests(List<ExecutedTest> tests) {
        this.test.addAll(tests);
    }

    void merge(SubjectCoverage subjectCoverage) {
        this.test.addAll(subjectCoverage.test);
    }
}
