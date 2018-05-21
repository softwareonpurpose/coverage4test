package com.softwareonpurpose.traceability4test;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class IntraAppRequirementTest {
    @Test
    public void toString_json() {
        String expected = "{\"id\":\"requirement_id\",\"test\":[{\"description\":\"any test\"}]}";
        String actual = new IntraAppRequirement("requirement_id", ExecutedTest.create("any test")).toString();
        Assert.assertEquals(actual, expected, "Failed to return expected json");
    }
}
