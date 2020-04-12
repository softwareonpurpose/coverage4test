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
 * Defines a System Requirement as a Requirement 'ID' and a list of SubjectCoverages from tests assumed to have
 * verified the required feature or functionality
 */
class SystemRequirement implements Comparable<SystemRequirement> {
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final String id;
    private SortedSet<TestedSubject> subjects = new TreeSet<>();

    private SystemRequirement(String requirement_id, Collection<TestedSubject> testSubjects) {
        this.id = requirement_id;
        for (TestedSubject subject : testSubjects) {
            addTestedSubject(subject);
        }
    }

    /**
     * Returns an instance of SystemRequirement initialized with a Requirement ID and coverage of a test subject
     *
     * @param requirementId String ID of the requirement covered
     * @param testedSubject TestedSubject verifying the requirement
     * @return An instance of SystemRequirement
     */
    static SystemRequirement getInstance(String requirementId, TestedSubject testedSubject) {
        List<TestedSubject> testSubjects = new ArrayList<>();
        if (testedSubject != null) {
            testSubjects.add(testedSubject);
        }
        return new SystemRequirement(requirementId, testSubjects);
    }

    /**
     * Returns an instance of SystemRequirement initialized with a Requirement ID and tested subjects
     *
     * @param requirementId  String ID of the requirement covered
     * @param testedSubjects Subjects tested, verifying the requirement
     * @return An instance of SystemRequirement
     */
    static SystemRequirement getInstance(String requirementId, Collection<TestedSubject> testedSubjects) {
        return new SystemRequirement(requirementId, testedSubjects);
    }

    /**
     * Adds coverage of a test subject which verifies the SystemRequirement
     *
     * @param testedSubject TestedSubject verifying the requirement
     */
    void addTestedSubject(TestedSubject testedSubject) {
        if (testedSubject == null) {
            return;
        }
        if (this.subjects.contains(testedSubject)) {
            List<TestedSubject> subjects = new ArrayList<>(this.subjects);
            int index = subjects.indexOf(testedSubject);
            testedSubject.merge(subjects.get(index));
            this.subjects.remove(testedSubject);
        }
        this.subjects.add(testedSubject);
    }

    /**
     * Adds coverage of a multiple test subjects which verifies the SystemRequirement
     *
     * @param testedSubjects Collection of SubjectCoverages verifying the requirement
     */
    void addTestedSubjects(Collection<TestedSubject> testedSubjects) {
        if (testedSubjects == null) {
            return;
        }
        for (TestedSubject subject : testedSubjects) {
            addTestedSubject(subject);
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null){return false;}
        if (obj == this) {
            return true;
        }
        if(!this.getClass().equals(obj.getClass())){
            return false;
        }
        SystemRequirement comparator = (SystemRequirement) obj;
        return new EqualsBuilder().append(this.id, comparator.id).isEquals();
    }

    @Override
    public int compareTo(SystemRequirement comparator) {
        if(comparator == null){
            return -1;
        }
        if(this.id == null && comparator.id == null){
            return 0;
        }
        return this.id == null ? -1
                : comparator.id == null ? 1
                : this.id.compareTo(comparator.id);
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Collection.class, new CollectionSerializer())
                .registerTypeHierarchyAdapter(Map.class, new MapSerializer())
                .setPrettyPrinting()
                .create();
        return gson.toJson(this);
    }

    public Collection<TestedSubject> getTestedSubjects() {
        return subjects;
    }

    public int getSubjectCount() {
        return subjects.size();
    }

    public int getTestExecutionCount() {
        int testCount = 0;
        for (TestedSubject subject : subjects) {
            testCount += subject.getTestCount();
        }
        return testCount;
    }
}
