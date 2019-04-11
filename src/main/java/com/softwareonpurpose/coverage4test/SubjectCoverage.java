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
import com.softwareonpurpose.coverage4test.serializer.CollectionSerializer;
import com.softwareonpurpose.coverage4test.serializer.MapSerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;

/**
 * Coverage of a test subject
 */
class SubjectCoverage implements Comparable<SubjectCoverage> {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String subject;
    private final Map<String, ExecutedTest> tests = new TreeMap<>();

    private SubjectCoverage(String subject, Collection<ExecutedTest> tests) {
        this.subject = subject;
        this.addTests(tests);
    }

    /**
     * Get an instance of SubjectCoverage
     * @param subjectDescription String description of the subject tested
     * @return SubjectCoverage
     */
    static SubjectCoverage getInstance(String subjectDescription) {
        return new SubjectCoverage(subjectDescription, new ArrayList<>());
    }

    /**
     * Get an instance of SubjectCoverage
     * @param subjectDescription String description of the subject tested
     * @param test ExecutedTest to cover the described subject
     * @return SubjectCoverage
     */
    static SubjectCoverage getInstance(String subjectDescription, ExecutedTest test) {
        return new SubjectCoverage(subjectDescription, Collections.singletonList(test));
    }

    /**
     * Get an instance of SubjectCoverage
     * @param subjectDescription String description of the subject tested
     * @param tests Collection of ExecutedTests covering the described subject
     * @return SubjectCoverage
     */
    static SubjectCoverage getInstance(String subjectDescription, Collection<ExecutedTest> tests) {
        return new SubjectCoverage(subjectDescription, tests);
    }

    /**
     * A test executed to cover the subject
     * @param test ExecutedTest covering the subject
     */
    void addTest(ExecutedTest test) {
        if (this.tests.containsKey(test.test)) {
            this.tests.get(test.test).addScenarios(test.getScenarios());
        } else {
            this.tests.put(test.test, test);
        }
    }

    /**
     * A collection of ExecutedTests to cover the subject
     * @param tests Collection of ExecutedTests
     */
    void addTests(Collection<ExecutedTest> tests) {
        for (ExecutedTest test : tests) {
            addTest(test);
        }
    }

    /**
     * Merge the tests covering one subject with this subject
     * @param subjectCoverage SubjectCoverage to be merged
     */
    void merge(SubjectCoverage subjectCoverage) {
        addTests(subjectCoverage.tests.values());
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
        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Collection.class, new CollectionSerializer())
                .registerTypeHierarchyAdapter(Map.class, new MapSerializer())
                .create();
        return gson.toJson(this);
    }
}
