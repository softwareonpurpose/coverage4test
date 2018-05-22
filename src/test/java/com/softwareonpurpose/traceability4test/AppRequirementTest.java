package com.softwareonpurpose.traceability4test;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

@Test
public class AppRequirementTest {
    @Test
    public void toString_json() {
        String requirementId = "requirement_id";
        String subjectDescription = "covered subject";
        String testDescription = "test description";
        ExecutedTest test = ExecutedTest.create(testDescription);
        SubjectCoverage subjectCovered = SubjectCoverage.create(subjectDescription, test);
        String expected = String.format("{\"id\":\"%s\",\"subject\":[%s]}", requirementId, subjectCovered.toString());
        String actual = new AppRequirement("requirement_id", subjectCovered).toString();
        Assert.assertEquals(actual, expected, "Failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_json")
    public void create_withSimpleTest() {
        String requirementId = "requirement_id";
        String testSubject = "test subject";
        ExecutedTest test = ExecutedTest.create("any test");
        SubjectCoverage subjectCovered = SubjectCoverage.create(testSubject, test);
        String expected = String.format("{\"id\":\"%s\",\"subject\":[%s]}", requirementId, subjectCovered.toString());
        String actual = AppRequirement.create(requirementId, SubjectCoverage.create(testSubject, test)).toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }

    @Test(dependsOnMethods = "toString_json")
    public void addTestedSubject() {
        String requirementId = "requirement_id";
        ExecutedTest test_1 = ExecutedTest.create("test 1");
        ExecutedTest test_2 = ExecutedTest.create("test 2");
        SubjectCoverage coveredSubject_1 = SubjectCoverage.create("test subject", test_1);
        SubjectCoverage coveredSubject_2 = SubjectCoverage.create("test subject", test_2);
        SubjectCoverage expectedSubject = SubjectCoverage.create("test subject", Arrays.asList(test_1, test_2));
        String expected = String.format("{\"id\":\"%s\",\"subject\":[%s]}", requirementId, expectedSubject);
        AppRequirement requirement = AppRequirement.create(requirementId, coveredSubject_1);
        requirement.addSubjectCoverage(coveredSubject_2);
        String actual = requirement.toString();
        Assert.assertEquals(actual, expected, "toString() failed to include test from duplicate test subject");
    }

    @Test(dependsOnMethods = "addTestedSubject")
    public void addTestedSubjects() {
        String requirementId = "requirement_id";
        ExecutedTest test_1 = ExecutedTest.create("test 1");
        ExecutedTest test_2 = ExecutedTest.create("test 2");
        ExecutedTest test_3 = ExecutedTest.create("test 3");
        SubjectCoverage coveredSubject_1 = SubjectCoverage.create("test subject 1", test_1);
        SubjectCoverage coveredSubject_2 = SubjectCoverage.create("test subject 2", test_2);
        SubjectCoverage coveredSubject_3 = SubjectCoverage.create("test subject 3", test_3);
        String expected =
                String.format("{\"id\":\"%s\",\"subject\":[%s,%s,%s]}",
                        requirementId, coveredSubject_1, coveredSubject_2, coveredSubject_3);
        AppRequirement requirement = AppRequirement.create(requirementId, coveredSubject_1);
        requirement.addSubjectCoverage(Arrays.asList(coveredSubject_2, coveredSubject_3));
        String actual = requirement.toString();
        Assert.assertEquals(actual, expected, "toString() failed to include added test subjects");
    }
/*

    @Test
    public void create_withTests() {
        String requirementId = "requirement_id";
        String test_1 = "test 1";
        String test_2 = "test 2";
        String test_3 = "test 3";
        List<ExecutedTest> tests = Arrays.asList(ExecutedTest.create(test_1), ExecutedTest.create(test_2), ExecutedTest.create(test_3));
        String expected =
                String.format("{\"id\":\"%s\",\"test\":[{\"description\":\"%s\"},{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        requirementId, test_1, test_2, test_3);
        AppRequirement requirement = AppRequirement.create(requirementId, tests);
        String actual = requirement.toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }

    @Test(dependsOnMethods = "addTests")
    public void toString_complexTest() {
        String requirementId = "requirement_id";
        String description = "any test";
        String scenario_1 = "scenario 1";
        String scenario_2 = "scenario 2";
        List<String> scenarios = Arrays.asList(scenario_1, scenario_2);
        String expected =
                String.format("{\"id\":\"%s\",\"test\":[{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"}]}]}",
                        requirementId, description, scenario_1, scenario_2);
        String actual = AppRequirement.create(requirementId, ExecutedTest.create(description, scenarios)).toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }

    @Test(dependsOnMethods = "addTests")
    public void toString_multipleComplexTests() {
        String requirementId = "requirement_id";
        String test_a = "test A";
        String test_b = "test B";
        String scenario_1 = "scenario 1";
        String scenario_2 = "scenario 2";
        List<String> scenarios = Arrays.asList(scenario_1, scenario_2);
        List<ExecutedTest> tests = Arrays.asList(ExecutedTest.create(test_a, scenarios), ExecutedTest.create(test_b, scenarios));
        String expected =
                String.format("{\"id\":\"%s\",\"test\":[{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"}]},{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"}]}]}",
                        requirementId, test_a, scenario_1, scenario_2, test_b, scenario_1, scenario_2);
        String actual = AppRequirement.create(requirementId, tests).toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }
    */
}
