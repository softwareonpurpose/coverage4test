package com.softwareonpurpose.traceability4test;

import org.testng.Assert;
import org.testng.annotations.Test;

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
}
