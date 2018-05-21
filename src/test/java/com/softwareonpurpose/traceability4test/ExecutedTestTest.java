package com.softwareonpurpose.traceability4test;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class ExecutedTestTest {
    @Test
    public void ToString_toScenario(){
        String expected = "{\"test\":\"test description\"}";
        ExecutedTest test = ExecutedTest.create("test description");
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "ExecutedTestTest.toString() failed to return expected json");
    }
}
