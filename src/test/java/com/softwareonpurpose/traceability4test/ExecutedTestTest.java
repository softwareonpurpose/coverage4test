package com.softwareonpurpose.traceability4test;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

@Test
public class ExecutedTestTest {
    @Test
    public void toString_noScenario() {
        String expected = "{\"description\":\"test description\"}";
        ExecutedTest test = ExecutedTest.create("test description");
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "ExecutedTestTest.toString() failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_noScenario")
    public void toString_oneScenario() {
        String testDescription = "test description";
        String scenarioDescription = "scenario description";
        String expected =
                String.format("{\"description\":\"%s\",\"scenarios\":[{\"description\":\"%s\"}]}",
                        testDescription, scenarioDescription);
        ExecutedTest test = new ExecutedTest(testDescription, scenarioDescription);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "ExecutedTestTest.toString() failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_oneScenario")
    public void toString_multipleScenarios() {
        String testDescription = "test description";
        String firstScenarioDescription = "scenario A description";
        String secondScenarioDescription = "scenario B description";
        String expected =
                String.format("{\"description\":\"%s\",\"scenarios\":[{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testDescription, firstScenarioDescription, secondScenarioDescription);
        List<DataScenario> scenarioList = Arrays.asList(DataScenario.create(firstScenarioDescription), DataScenario.create(secondScenarioDescription));
        ExecutedTest test = new ExecutedTest(testDescription, scenarioList);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "ExecutedTestTest.toString() failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_noScenario")
    public void addOneScenario(){
        String scenarioDescription = "scenario description";
        String testDescription = "test description";
        String expected = String.format("{\"description\":\"%s\",\"scenarios\":[{\"description\":\"%s\"}]}", testDescription, scenarioDescription);
        ExecutedTest test = ExecutedTest.create(testDescription);
        test.addScenario(scenarioDescription);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to addScenario scenario");
    }
}
