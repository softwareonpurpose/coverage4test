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

import java.util.*;

class AppRequirement implements Comparable<AppRequirement> {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String id;
    private SortedSet<SubjectCoverage> subject = new TreeSet<>();

    private AppRequirement(String requirement_id, List<SubjectCoverage> testSubjects) {
        this.id = requirement_id;
        this.subject.addAll(testSubjects);
    }

    AppRequirement(String requirementId, SubjectCoverage subjectCovered) {
        this.id = requirementId;
        this.subject.add(subjectCovered);
    }

    static AppRequirement construct(String description, List<SubjectCoverage> subjectCoverage) {
        return new AppRequirement(description, subjectCoverage);
    }

    static AppRequirement construct(String requirementId, SubjectCoverage subjectCoverage) {
        return new AppRequirement(requirementId, subjectCoverage);
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
        if (!(obj instanceof AppRequirement)) {
            return false;
        }
        AppRequirement comparator = (AppRequirement) obj;
        return new EqualsBuilder().append(this.id, comparator.id).isEquals();
    }

    @Override
    public int compareTo(AppRequirement comparator) {
        return this.id == null && comparator.id == null ? 0
                : this.id == null ? -1
                : comparator.id == null ? 1
                : this.id.compareTo(comparator.id);
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(
                Collection.class, new CollectionSerializer()).create();
        return gson.toJson(this);
    }

    void addSubjectCoverage(SubjectCoverage subjectCoverage) {
        if (this.subject.contains(subjectCoverage)) {
            ArrayList<SubjectCoverage> subjects = new ArrayList<>(this.subject);
            int index = subjects.indexOf(subjectCoverage);
            subjectCoverage.merge(subjects.get(index));
            this.subject.remove(subjectCoverage);
        }
        this.subject.add(subjectCoverage);
    }

    void addSubjectCoverage(List<SubjectCoverage> subjectCoverage) {
        for (SubjectCoverage subject : subjectCoverage) {
            addSubjectCoverage(subject);
        }
    }
}
