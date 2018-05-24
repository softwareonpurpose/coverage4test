package com.softwareonpurpose.traceability4test;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

@Test
public class AppRequirementTest {
    @Test
    public void toString_json() {
        String requirementId = "requirement_id";
        String subjectDescription = "covered subject";
        String testDescription = "test description";
        ExecutedTest test = ExecutedTest.construct(testDescription);
        SubjectCoverage subjectCovered = SubjectCoverage.construct(subjectDescription, test);
        String expected = String.format("{\"id\":\"%s\",\"subject\":[%s]}", requirementId, subjectCovered.toString());
        String actual = new AppRequirement("requirement_id", subjectCovered).toString();
        Assert.assertEquals(actual, expected, "Failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_json")
    public void create_withSimpleTest() {
        String requirementId = "requirement_id";
        String testSubject = "test subject";
        ExecutedTest test = ExecutedTest.construct("any test");
        SubjectCoverage subjectCovered = SubjectCoverage.construct(testSubject, test);
        String expected = String.format("{\"id\":\"%s\",\"subject\":[%s]}", requirementId, subjectCovered.toString());
        String actual = AppRequirement.construct(requirementId, SubjectCoverage.construct(testSubject, test)).toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }

    @Test(dependsOnMethods = "toString_json")
    public void addTestedSubject() {
        String requirementId = "requirement_id";
        ExecutedTest test_1 = ExecutedTest.construct("test 1");
        ExecutedTest test_2 = ExecutedTest.construct("test 2");
        SubjectCoverage coveredSubject_1 = SubjectCoverage.construct("test subject", test_1);
        SubjectCoverage coveredSubject_2 = SubjectCoverage.construct("test subject", test_2);
        SubjectCoverage expectedSubject = SubjectCoverage.construct("test subject", Arrays.asList(test_1, test_2));
        String expected = String.format("{\"id\":\"%s\",\"subject\":[%s]}", requirementId, expectedSubject);
        AppRequirement requirement = AppRequirement.construct(requirementId, coveredSubject_1);
        requirement.addSubjectCoverage(coveredSubject_2);
        String actual = requirement.toString();
        Assert.assertEquals(actual, expected, "toString() failed to include test from duplicate test subject");
    }

    @Test(dependsOnMethods = "addTestedSubject")
    public void addTestedSubjects() {
        String requirementId = "requirement_id";
        ExecutedTest test_1 = ExecutedTest.construct("test 1");
        ExecutedTest test_2 = ExecutedTest.construct("test 2");
        ExecutedTest test_3 = ExecutedTest.construct("test 3");
        SubjectCoverage coveredSubject_1 = SubjectCoverage.construct("test subject 1", test_1);
        SubjectCoverage coveredSubject_2 = SubjectCoverage.construct("test subject 2", test_2);
        SubjectCoverage coveredSubject_3 = SubjectCoverage.construct("test subject 3", test_3);
        String expected =
                String.format("{\"id\":\"%s\",\"subject\":[%s,%s,%s]}",
                        requirementId, coveredSubject_1, coveredSubject_2, coveredSubject_3);
        AppRequirement requirement = AppRequirement.construct(requirementId, coveredSubject_1);
        requirement.addSubjectCoverage(Arrays.asList(coveredSubject_2, coveredSubject_3));
        String actual = requirement.toString();
        Assert.assertEquals(actual, expected, "toString() failed to include added test subjects");
    }

    @Test
    public void create_withTestSubjects() {
        String requirementId = "requirement_id";
        String test_1 = "test 1";
        ExecutedTest executedTest = ExecutedTest.construct(test_1);
        SubjectCoverage subject_1 = SubjectCoverage.construct("subject 1", executedTest);
        SubjectCoverage subject_2 = SubjectCoverage.construct("subject 2", executedTest);
        SubjectCoverage subject_3 = SubjectCoverage.construct("subject 3", executedTest);
        List<SubjectCoverage> subjectsCovered = Arrays.asList(subject_1, subject_2, subject_3);
        String expected =
                String.format("{\"id\":\"%s\",\"subject\":[%s,%s,%s]}", requirementId, subject_1, subject_2, subject_3);
        AppRequirement requirement = AppRequirement.construct(requirementId, subjectsCovered);
        String actual = requirement.toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }
}
