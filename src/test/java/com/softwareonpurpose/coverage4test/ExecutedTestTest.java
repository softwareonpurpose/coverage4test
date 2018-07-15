package com.softwareonpurpose.coverage4test;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

@Test
public class ExecutedTestTest {
    @Test
    public void toString_noScenario() {
        String expected = "{\"test\":\"test test\"}";
        ExecutedTest test = ExecutedTest.getInstance("test test");
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "ExecutedTestTest.toString() failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_noScenario")
    public void toString_oneScenario() {
        String testDescription = "test test";
        String scenarioDescription = "scenario test";
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\"]}";
        String expected = String.format(expectedFormat, testDescription, scenarioDescription);
        ExecutedTest test = ExecutedTest.getInstance(testDescription, scenarioDescription);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "ExecutedTestTest.toString() failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_oneScenario")
    public void toString_multipleScenarios() {
        String testDescription = "test test";
        String firstScenarioDescription = "scenario A test";
        String secondScenarioDescription = "scenario B test";
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, testDescription, firstScenarioDescription, secondScenarioDescription);
        List<String> scenarioList = Arrays.asList(firstScenarioDescription, secondScenarioDescription);
        ExecutedTest test = ExecutedTest.getInstance(testDescription, scenarioList);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "ExecutedTestTest.toString() failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_noScenario")
    public void addOneScenario() {
        String testDescription = "test test";
        String scenarioDescription = "scenario test";
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\"]}";
        String expected = String.format(expectedFormat, testDescription, scenarioDescription);
        ExecutedTest test = ExecutedTest.getInstance(testDescription);
        test.addScenario(scenarioDescription);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario");
    }

    @Test
    public void initializeWithScenario() {
        String testDescription = "test test";
        String scenarioDescription = "initializing scenario";
        String expected = String.format("{\"test\":\"%s\",\"scenarios\":[\"%s\"]}", testDescription, scenarioDescription);
        ExecutedTest test = ExecutedTest.getInstance(testDescription, scenarioDescription);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to initialize with scenario");
    }

    @Test(dependsOnMethods = "initializeWithScenario")
    public void initializeWithScenarioAddScenario() {
        String testDescription = "test test";
        String initialScenario = "scenario 1";
        String secondScenario = "scenario 2";
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, testDescription, initialScenario, secondScenario);
        ExecutedTest test = ExecutedTest.getInstance(testDescription, initialScenario);
        test.addScenario(secondScenario);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add second scenario after initializing with first");
    }

    @Test
    public void initializeWithScenarios() {
        String testDescription = "test test";
        String scenario_1 = "first scenario";
        String scenario_2 = "second scenario";
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, testDescription, scenario_1, scenario_2);
        ExecutedTest test = ExecutedTest.getInstance(testDescription, Arrays.asList(scenario_1, scenario_2));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to initialize with list of scenarios");
    }

    @Test(dependsOnMethods = {"initializeWithScenarios", "initializeWithScenarioAddScenario"})
    public void initializeWithScenariosAddScenario() {
        String testDescription = "test test";
        String scenario_1 = "first scenario";
        String scenario_2 = "second scenario";
        String scenario_3 = "third scenario";
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, testDescription, scenario_1, scenario_2, scenario_3);
        ExecutedTest test = ExecutedTest.getInstance(testDescription, Arrays.asList(scenario_1, scenario_2));
        test.addScenario(scenario_3);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to successfully add scenario after initializing with scenario list");
    }

    @Test
    public void addScenarios() {
        String testDescription = "test test";
        String scenario_1 = "first scenario";
        String scenario_2 = "second scenario";
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, testDescription, scenario_1, scenario_2);
        ExecutedTest test = ExecutedTest.getInstance(testDescription);
        test.addScenarios(Arrays.asList(scenario_1, scenario_2));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list");
    }

    @Test(dependsOnMethods = {"initializeWithScenario"})
    public void initializeWithScenarioAddScenarios() {
        String testDescription = "test test";
        String initialScenario = "initializing scenario";
        String scenario_2 = "second scenario";
        String scenario_3 = "third scenario";
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, testDescription, initialScenario, scenario_2, scenario_3);
        ExecutedTest test = ExecutedTest.getInstance(testDescription, initialScenario);
        test.addScenarios(Arrays.asList(scenario_2, scenario_3));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"initializeWithScenarios", "addScenarios"})
    public void initializeWithScenariosAddScenarios() {
        String testDescription = "test test";
        String scenario_1 = "scenario 1";
        String scenario_2 = "scenario 2";
        String scenario_3 = "scenario 3";
        String scenario_4 = "scenario 4";
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\",\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, testDescription, scenario_1, scenario_2, scenario_3, scenario_4);
        ExecutedTest test = ExecutedTest.getInstance(testDescription, Arrays.asList(scenario_1, scenario_2));
        test.addScenarios(Arrays.asList(scenario_3, scenario_4));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"addOneScenario", "addScenarios"})
    public void addScenariosAfterAddScenario() {
        String testDescription = "test test";
        String scenario_1 = "first scenario";
        String scenario_2 = "second scenario";
        String scenario_3 = "third scenario";
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, testDescription, scenario_1, scenario_2, scenario_3);
        ExecutedTest test = ExecutedTest.getInstance(testDescription);
        test.addScenario(scenario_1);
        test.addScenarios(Arrays.asList(scenario_2, scenario_3));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"addOneScenario", "addScenarios"})
    public void addScenarioAfterAddScenarios() {
        String testDescription = "test test";
        String scenario_1 = "first scenario";
        String scenario_2 = "second scenario";
        String scenario_3 = "third scenario";
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, testDescription, scenario_1, scenario_2, scenario_3);
        ExecutedTest test = ExecutedTest.getInstance(testDescription);
        test.addScenarios(Arrays.asList(scenario_1, scenario_2));
        test.addScenario(scenario_3);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"addScenarios"})
    public void addScenariosAfterAddScenarios() {
        String testDescription = "test test";
        String scenario_1 = "scenario 1";
        String scenario_2 = "scenario 2";
        String scenario_3 = "scenario 3";
        String scenario_4 = "scenario 4";
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\",\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, testDescription, scenario_1, scenario_2, scenario_3, scenario_4);
        ExecutedTest test = ExecutedTest.getInstance(testDescription);
        test.addScenarios(Arrays.asList(scenario_1, scenario_2));
        test.addScenarios(Arrays.asList(scenario_3, scenario_4));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"addScenarios"})
    public void toString_scenarioOrder() {
        String testDescription = "test test";
        String scenario_1 = "scenario 1";
        String scenario_2 = "scenario 2";
        String scenario_3 = "scenario 3";
        String scenario_4 = "scenario 4";
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\",\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, testDescription, scenario_1, scenario_2, scenario_3, scenario_4);
        ExecutedTest test = ExecutedTest.getInstance(testDescription, Arrays.asList(scenario_4, scenario_3, scenario_2, scenario_1));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "toString() failed to sort the scenario list alphabetically");
    }

    @Test(dependsOnMethods = {"addScenarios"})
    public void toString_ensureUniqueness() {
        String testDescription = "test test";
        String scenario_1 = "scenario A";
        String scenario_2 = "scenario B";
        String scenario_3 = "scenario B";
        String scenario_4 = "scenario A";
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, testDescription, scenario_1, scenario_2);
        ExecutedTest test = ExecutedTest.getInstance(testDescription, Arrays.asList(scenario_4, scenario_3, scenario_2, scenario_1));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "toString() failed to ensure uniqueness of the scenarios");
    }
}
