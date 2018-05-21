package com.softwareonpurpose.traceability4test;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class ExecutedTestTest {
    @Test
    public void toString_noScenario() {
        String expected = "{\"description\":\"test description\"}";
        ExecutedTest test = ExecutedTest.create("test description");
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "ExecutedTestTest.toString() failed to return expected json");
    }

    @Test
    public void toString_oneScenario() {
        String expected = "{\"description\":\"test description\",\"scenarios\":[{\"description\":\"scenario description\"}]}";
        ExecutedTest test = new ExecutedTest("test description", "scenario description");
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "ExecutedTestTest.toString() failed to return expected json");
    }
}
