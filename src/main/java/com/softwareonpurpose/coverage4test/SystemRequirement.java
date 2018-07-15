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


class SystemRequirement implements Comparable<SystemRequirement> {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String id;
    private SortedSet<SubjectCoverage> subjects = new TreeSet<>();

    private SystemRequirement(String requirement_id, Collection<SubjectCoverage> testSubjects) {
        this.id = requirement_id;
        this.subjects.addAll(testSubjects);
    }

    static SystemRequirement construct(String requirementId, Collection<SubjectCoverage> subjectCoverage) {
        return new SystemRequirement(requirementId, subjectCoverage);
    }

    static SystemRequirement construct(String requirementId, SubjectCoverage subjectCoverage) {
        return new SystemRequirement(requirementId, Collections.singletonList(subjectCoverage));
    }

    void addSubjectCoverage(SubjectCoverage subjectCoverage) {
        if (this.subjects.contains(subjectCoverage)) {
            List<SubjectCoverage> subjects = new ArrayList<>(this.subjects);
            int index = subjects.indexOf(subjectCoverage);
            subjectCoverage.merge(subjects.get(index));
            this.subjects.remove(subjectCoverage);
        }
        this.subjects.add(subjectCoverage);
    }

    void addSubjectCoverage(Collection<SubjectCoverage> subjectCoverage) {
        for (SubjectCoverage subject : subjectCoverage) {
            addSubjectCoverage(subject);
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SystemRequirement)) {
            return false;
        }
        SystemRequirement comparator = (SystemRequirement) obj;
        return new EqualsBuilder().append(this.id, comparator.id).isEquals();
    }

    @Override
    public int compareTo(SystemRequirement comparator) {
        return this.id == null && comparator.id == null ? 0
                : this.id == null ? -1
                : comparator.id == null ? 1
                : this.id.compareTo(comparator.id);
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
