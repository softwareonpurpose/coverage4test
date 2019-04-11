package com.softwareonpurpose.coverage4test;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

@Test
public class SystemRequirementTest {
    private static final String REQUIREMENT_ID = "requirement_id";
    private static final String DEFAULT_FAILURE_MESSAGE = "Failed to return expected json";
    @Test
    public void toString_json() {
        String subjectDescription = "covered subject";
        String testDescription = "test";
        ExecutedTest test = ExecutedTest.getInstance(testDescription);
        SubjectCoverage subjectCovered = SubjectCoverage.getInstance(subjectDescription, test);
        String expected = String.format("{\"id\":\"%s\",\"subjects\":[%s]}", REQUIREMENT_ID, subjectCovered.toString());
        String actual = SystemRequirement.getInstance(REQUIREMENT_ID, subjectCovered).toString();
        Assert.assertEquals(actual, expected, DEFAULT_FAILURE_MESSAGE);
    }

    @Test(dependsOnMethods = "toString_json")
    public void create_withSimpleTest() {
        String testSubject = "subject";
        ExecutedTest test = ExecutedTest.getInstance("any test");
        SubjectCoverage subjectCovered = SubjectCoverage.getInstance(testSubject, test);
        String expected = String.format("{\"id\":\"%s\",\"subjects\":[%s]}", REQUIREMENT_ID, subjectCovered.toString());
        String actual = SystemRequirement.getInstance(REQUIREMENT_ID, SubjectCoverage.getInstance(testSubject, test)).toString();
        Assert.assertEquals(actual, expected, DEFAULT_FAILURE_MESSAGE);
    }

    @Test(dependsOnMethods = "toString_json")
    public void addTestedSubject() {
        ExecutedTest test_1 = ExecutedTest.getInstance("test 1");
        ExecutedTest test_2 = ExecutedTest.getInstance("test 2");
        SubjectCoverage coveredSubject_1 = SubjectCoverage.getInstance("subject", test_1);
        SubjectCoverage coveredSubject_2 = SubjectCoverage.getInstance("subject", test_2);
        SubjectCoverage expectedSubject = SubjectCoverage.getInstance("subject", Arrays.asList(test_1, test_2));
        String expected = String.format("{\"id\":\"%s\",\"subjects\":[%s]}", REQUIREMENT_ID, expectedSubject);
        SystemRequirement requirement = SystemRequirement.getInstance(REQUIREMENT_ID, coveredSubject_1);
        requirement.addSubjectCoverage(coveredSubject_2);
        String actual = requirement.toString();
        Assert.assertEquals(actual, expected, "toString() failed to include test from duplicate subject");
    }

    @Test(dependsOnMethods = "addTestedSubject")
    public void addTestedSubjects() {
        ExecutedTest test_1 = ExecutedTest.getInstance("test 1");
        ExecutedTest test_2 = ExecutedTest.getInstance("test 2");
        ExecutedTest test_3 = ExecutedTest.getInstance("test 3");
        SubjectCoverage coveredSubject_1 = SubjectCoverage.getInstance("subject 1", test_1);
        SubjectCoverage coveredSubject_2 = SubjectCoverage.getInstance("subject 2", test_2);
        SubjectCoverage coveredSubject_3 = SubjectCoverage.getInstance("subject 3", test_3);
        String expected =
                String.format("{\"id\":\"%s\",\"subjects\":[%s,%s,%s]}",
                        REQUIREMENT_ID, coveredSubject_1, coveredSubject_2, coveredSubject_3);
        SystemRequirement requirement = SystemRequirement.getInstance(REQUIREMENT_ID, coveredSubject_1);
        requirement.addSubjectCoverage(Arrays.asList(coveredSubject_2, coveredSubject_3));
        String actual = requirement.toString();
        Assert.assertEquals(actual, expected, "toString() failed to include added subjects");
    }

    @Test
    public void create_withTestSubjects() {
        String test_1 = "test 1";
        ExecutedTest executedTest = ExecutedTest.getInstance(test_1);
        SubjectCoverage subject_1 = SubjectCoverage.getInstance("subject 1", executedTest);
        SubjectCoverage subject_2 = SubjectCoverage.getInstance("subject 2", executedTest);
        SubjectCoverage subject_3 = SubjectCoverage.getInstance("subject 3", executedTest);
        List<SubjectCoverage> subjectsCovered = Arrays.asList(subject_1, subject_2, subject_3);
        String expected =
                String.format("{\"id\":\"%s\",\"subjects\":[%s,%s,%s]}", REQUIREMENT_ID, subject_1, subject_2, subject_3);
        SystemRequirement requirement = SystemRequirement.getInstance(REQUIREMENT_ID, subjectsCovered);
        String actual = requirement.toString();
        Assert.assertEquals(actual, expected, DEFAULT_FAILURE_MESSAGE);
    }
}
