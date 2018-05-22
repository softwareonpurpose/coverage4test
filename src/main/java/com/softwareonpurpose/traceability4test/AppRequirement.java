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

class AppRequirement {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String id;
    private List<SubjectCoverage> subject = new ArrayList<>();

    private AppRequirement(String requirement_id, List<SubjectCoverage> testSubjects) {
        this.id = requirement_id;
        this.subject.addAll(testSubjects);
    }

    AppRequirement(String requirementId, SubjectCoverage subjectCovered) {
        this.id = requirementId;
        this.subject.add(subjectCovered);
    }

    static AppRequirement create(String description, List<SubjectCoverage> subjectCoverage) {
        return new AppRequirement(description, subjectCoverage);
    }

    public static AppRequirement create(String requirementId, SubjectCoverage subjectCoverage) {
        return new AppRequirement(requirementId, subjectCoverage);
    }

    @Override
    public String toString() {
        sortCollections();
        subject = subject.stream().distinct().collect(Collectors.toList());
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(
                Collection.class, new CollectionSerializer()).create();
        return gson.toJson(this);
    }

    private void sortCollections() {
        Collections.sort(subject);
        for (SubjectCoverage subject : subject) {
            subject.softCollections();
        }
    }

    void addSubjectCoverage(SubjectCoverage subjectCoverage) {
        if (subject.contains(subjectCoverage)) {
            int index = subject.indexOf(subjectCoverage);
            subjectCoverage.addTests(subject.get(index).getTests());
            subject.set(index, subjectCoverage);
        } else {
            this.subject.add(subjectCoverage);
        }
    }

    void addSubjectCoverage(List<SubjectCoverage> subjectCoverage) {
        for (SubjectCoverage subject : subjectCoverage) {
            addSubjectCoverage(subject);
        }
    }
}
