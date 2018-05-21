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
                String.format("{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"}]}",
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
                String.format("{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testDescription, firstScenarioDescription, secondScenarioDescription);
        List<DataScenario> scenarioList = Arrays.asList(DataScenario.create(firstScenarioDescription), DataScenario.create(secondScenarioDescription));
        ExecutedTest test = new ExecutedTest(testDescription, scenarioList);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "ExecutedTestTest.toString() failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_noScenario")
    public void addOneScenario() {
        String testDescription = "test description";
        String scenarioDescription = "scenario description";
        String expected =
                String.format("{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"}]}",
                        testDescription, scenarioDescription);
        ExecutedTest test = ExecutedTest.create(testDescription);
        test.addScenario(scenarioDescription);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario");
    }

    @Test
    public void initializeWithScenario() {
        String testDescription = "test description";
        String scenarioDescription = "initializing scenario";
        String expected =
                String.format("{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"}]}",
                        testDescription, scenarioDescription);
        ExecutedTest test = ExecutedTest.create(testDescription, scenarioDescription);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to initialize with scenario");
    }

    @Test(dependsOnMethods = "initializeWithScenario")
    public void initializeWithScenarioAddScenario() {
        String testDescription = "test description";
        String initialScenario = "scenario 1";
        String secondScenario = "scenario 2";
        String expected =
                String.format("{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testDescription, initialScenario, secondScenario);
        ExecutedTest test = ExecutedTest.create(testDescription, initialScenario);
        test.addScenario(secondScenario);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add second scenario after initializing with first");
    }

    @Test
    public void initializeWithScenarios() {
        String testDescription = "test description";
        String scenario_1 = "first scenario";
        String scenario_2 = "second scenario";
        String expected =
                String.format("{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testDescription, scenario_1, scenario_2);
        ExecutedTest test = ExecutedTest.create(testDescription, Arrays.asList(scenario_1, scenario_2));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to initialize with list of scenarios");
    }

    @Test(dependsOnMethods = {"initializeWithScenarios", "initializeWithScenarioAddScenario"})
    public void initializeWithScenariosAddScenario() {
        String testDescription = "test description";
        String scenario_1 = "first scenario";
        String scenario_2 = "second scenario";
        String scenario_3 = "third scenario";
        String expected =
                String.format(
                        "{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testDescription, scenario_1, scenario_2, scenario_3);
        ExecutedTest test = ExecutedTest.create(testDescription, Arrays.asList(scenario_1, scenario_2));
        test.addScenario(scenario_3);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to successfully add scenario after initializing with scenario list");
    }

    @Test
    public void addScenarios() {
        String testDescription = "test description";
        String scenario_1 = "first scenario";
        String scenario_2 = "second scenario";
        String expected =
                String.format("{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testDescription, scenario_1, scenario_2);
        ExecutedTest test = ExecutedTest.create(testDescription);
        test.addScenarios(Arrays.asList(scenario_1, scenario_2));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list");
    }

    @Test(dependsOnMethods = {"initializeWithScenario"})
    public void initializeWithScenarioAddScenarios() {
        String testDescription = "test description";
        String initialScenario = "initializing scenario";
        String scenario_2 = "second scenario";
        String scenario_3 = "third scenario";
        String expected =
                String.format("{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testDescription, initialScenario, scenario_2, scenario_3);
        ExecutedTest test = ExecutedTest.create(testDescription, initialScenario);
        test.addScenarios(Arrays.asList(scenario_2, scenario_3));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"initializeWithScenarios", "addScenarios"})
    public void initializeWithScenariosAddScenarios() {
        String testDescription = "test description";
        String scenario_1 = "scenario 1";
        String scenario_2 = "scenario 2";
        String scenario_3 = "scenario 3";
        String scenario_4 = "scenario 4";
        String expected =
                String.format("{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"},{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testDescription, scenario_1, scenario_2, scenario_3, scenario_4);
        ExecutedTest test = ExecutedTest.create(testDescription, Arrays.asList(scenario_1, scenario_2));
        test.addScenarios(Arrays.asList(scenario_3, scenario_4));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"addOneScenario", "addScenarios"})
    public void addScenariosAfterAddScenario() {
        String testDescription = "test description";
        String scenario_1 = "first scenario";
        String scenario_2 = "second scenario";
        String scenario_3 = "third scenario";
        String expected =
                String.format("{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testDescription, scenario_1, scenario_2, scenario_3);
        ExecutedTest test = ExecutedTest.create(testDescription);
        test.addScenario(scenario_1);
        test.addScenarios(Arrays.asList(scenario_2, scenario_3));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"addOneScenario", "addScenarios"})
    public void addScenarioAfterAddScenarios() {
        String testDescription = "test description";
        String scenario_1 = "first scenario";
        String scenario_2 = "second scenario";
        String scenario_3 = "third scenario";
        String expected =
                String.format("{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testDescription, scenario_1, scenario_2, scenario_3);
        ExecutedTest test = ExecutedTest.create(testDescription);
        test.addScenarios(Arrays.asList(scenario_1, scenario_2));
        test.addScenario(scenario_3);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"addScenarios"})
    public void addScenariosAfterAddScenarios() {
        String testDescription = "test description";
        String scenario_1 = "scenario 1";
        String scenario_2 = "scenario 2";
        String scenario_3 = "scenario 3";
        String scenario_4 = "scenario 4";
        String expected =
                String.format("{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"},{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testDescription, scenario_1, scenario_2, scenario_3, scenario_4);
        ExecutedTest test = ExecutedTest.create(testDescription);
        test.addScenarios(Arrays.asList(scenario_1, scenario_2));
        test.addScenarios(Arrays.asList(scenario_3, scenario_4));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"addScenarios"})
    public void toString_scenarioOrder() {
        String testDescription = "test description";
        String scenario_1 = "scenario 1";
        String scenario_2 = "scenario 2";
        String scenario_3 = "scenario 3";
        String scenario_4 = "scenario 4";
        String expected =
                String.format("{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"},{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testDescription, scenario_1, scenario_2, scenario_3, scenario_4);
        ExecutedTest test =
                ExecutedTest.create(testDescription, Arrays.asList(scenario_4, scenario_3, scenario_2, scenario_1));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "toString() failed to sort the scenario list alphabetically");
    }

    @Test(dependsOnMethods = {"addScenarios"})
    public void toString_ensureUniqueness() {
        String testDescription = "test description";
        String scenario_1 = "scenario A";
        String scenario_2 = "scenario B";
        String scenario_3 = "scenario B";
        String scenario_4 = "scenario A";
        String expected =
                String.format("{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testDescription, scenario_1, scenario_2);
        ExecutedTest test =
                ExecutedTest.create(testDescription, Arrays.asList(scenario_4, scenario_3, scenario_2, scenario_1));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "toString() failed to ensure uniqueness of the scenarios");
    }
}
