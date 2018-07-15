package com.softwareonpurpose.coverage4test;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

@Test
public class SystemRequirementTest {
    @Test
    public void toString_json() {
        String requirementId = "requirement_id";
        String subjectDescription = "covered subject";
        String testDescription = "test test";
        ExecutedTest test = ExecutedTest.getInstance(testDescription);
        SubjectCoverage subjectCovered = SubjectCoverage.getInstance(subjectDescription, test);
        String expected = String.format("{\"id\":\"%s\",\"subjects\":[%s]}", requirementId, subjectCovered.toString());
        String actual = SystemRequirement.getInstance("requirement_id", subjectCovered).toString();
        Assert.assertEquals(actual, expected, "Failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_json")
    public void create_withSimpleTest() {
        String requirementId = "requirement_id";
        String testSubject = "test subject";
        ExecutedTest test = ExecutedTest.getInstance("any test");
        SubjectCoverage subjectCovered = SubjectCoverage.getInstance(testSubject, test);
        String expected = String.format("{\"id\":\"%s\",\"subjects\":[%s]}", requirementId, subjectCovered.toString());
        String actual = SystemRequirement.getInstance(requirementId, SubjectCoverage.getInstance(testSubject, test)).toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }

    @Test(dependsOnMethods = "toString_json")
    public void addTestedSubject() {
        String requirementId = "requirement_id";
        ExecutedTest test_1 = ExecutedTest.getInstance("test 1");
        ExecutedTest test_2 = ExecutedTest.getInstance("test 2");
        SubjectCoverage coveredSubject_1 = SubjectCoverage.getInstance("test subject", test_1);
        SubjectCoverage coveredSubject_2 = SubjectCoverage.getInstance("test subject", test_2);
        SubjectCoverage expectedSubject = SubjectCoverage.getInstance("test subject", Arrays.asList(test_1, test_2));
        String expected = String.format("{\"id\":\"%s\",\"subjects\":[%s]}", requirementId, expectedSubject);
        SystemRequirement requirement = SystemRequirement.getInstance(requirementId, coveredSubject_1);
        requirement.addSubjectCoverage(coveredSubject_2);
        String actual = requirement.toString();
        Assert.assertEquals(actual, expected, "toString() failed to include test from duplicate test subject");
    }

    @Test(dependsOnMethods = "addTestedSubject")
    public void addTestedSubjects() {
        String requirementId = "requirement_id";
        ExecutedTest test_1 = ExecutedTest.getInstance("test 1");
        ExecutedTest test_2 = ExecutedTest.getInstance("test 2");
        ExecutedTest test_3 = ExecutedTest.getInstance("test 3");
        SubjectCoverage coveredSubject_1 = SubjectCoverage.getInstance("test subject 1", test_1);
        SubjectCoverage coveredSubject_2 = SubjectCoverage.getInstance("test subject 2", test_2);
        SubjectCoverage coveredSubject_3 = SubjectCoverage.getInstance("test subject 3", test_3);
        String expected =
                String.format("{\"id\":\"%s\",\"subjects\":[%s,%s,%s]}",
                        requirementId, coveredSubject_1, coveredSubject_2, coveredSubject_3);
        SystemRequirement requirement = SystemRequirement.getInstance(requirementId, coveredSubject_1);
        requirement.addSubjectCoverage(Arrays.asList(coveredSubject_2, coveredSubject_3));
        String actual = requirement.toString();
        Assert.assertEquals(actual, expected, "toString() failed to include added test subjects");
    }

    @Test
    public void create_withTestSubjects() {
        String requirementId = "requirement_id";
        String test_1 = "test 1";
        ExecutedTest executedTest = ExecutedTest.getInstance(test_1);
        SubjectCoverage subject_1 = SubjectCoverage.getInstance("subject 1", executedTest);
        SubjectCoverage subject_2 = SubjectCoverage.getInstance("subject 2", executedTest);
        SubjectCoverage subject_3 = SubjectCoverage.getInstance("subject 3", executedTest);
        List<SubjectCoverage> subjectsCovered = Arrays.asList(subject_1, subject_2, subject_3);
        String expected =
                String.format("{\"id\":\"%s\",\"subjects\":[%s,%s,%s]}", requirementId, subject_1, subject_2, subject_3);
        SystemRequirement requirement = SystemRequirement.getInstance(requirementId, subjectsCovered);
        String actual = requirement.toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }
}
