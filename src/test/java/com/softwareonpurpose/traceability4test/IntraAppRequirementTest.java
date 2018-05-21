package com.softwareonpurpose.traceability4test;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

@Test
public class IntraAppRequirementTest {
    @Test
    public void toString_json() {
        String requirementId = "requirement_id";
        String description = "any test";
        String expected = String.format("{\"id\":\"%s\",\"test\":[{\"description\":\"%s\"}]}", requirementId, description);
        String actual = new IntraAppRequirement("requirement_id", ExecutedTest.create(description)).toString();
        Assert.assertEquals(actual, expected, "Failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_json")
    public void create_withSimpleTest() {
        String requirementId = "requirement_id";
        String description = "any test";
        String expected = String.format("{\"id\":\"%s\",\"test\":[{\"description\":\"%s\"}]}", requirementId, description);
        String actual = IntraAppRequirement.create(requirementId, ExecutedTest.create(description)).toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }

    @Test(dependsOnMethods = "toString_json")
    public void addTest() {
        String requirementId = "requirement_id";
        String test_1 = "test 1";
        String test_2 = "test 2";
        String expected =
                String.format("{\"id\":\"%s\",\"test\":[{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        requirementId, test_1, test_2);
        IntraAppRequirement requirement = IntraAppRequirement.create(requirementId, ExecutedTest.create(test_1));
        requirement.addTest(test_2);
        String actual = requirement.toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }

    @Test(dependsOnMethods = "addTest")
    public void addTests() {
        String requirementId = "requirement_id";
        String test_1 = "test 1";
        String test_2 = "test 2";
        String test_3 = "test 3";
        List<ExecutedTest> tests = Arrays.asList(ExecutedTest.create(test_2), ExecutedTest.create(test_3));
        String expected =
                String.format("{\"id\":\"%s\",\"test\":[{\"description\":\"%s\"},{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        requirementId, test_1, test_2, test_3);
        IntraAppRequirement requirement = IntraAppRequirement.create(requirementId, ExecutedTest.create(test_1));
        requirement.addTests(tests);
        String actual = requirement.toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }

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
        IntraAppRequirement requirement = IntraAppRequirement.create(requirementId, tests);
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
        String actual = IntraAppRequirement.create(requirementId, ExecutedTest.create(description, scenarios)).toString();
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
        String actual = IntraAppRequirement.create(requirementId, tests).toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }
}
