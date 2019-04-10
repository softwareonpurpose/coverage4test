package com.softwareonpurpose.coverage4test;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

@Test
public class ExecutedTestTest {
    private static final String TEST_DESCRIPTION = "test test";
    private static final String SCENARIO_DESCRIPTION = "scenario %S";
    @Test
    public void toString_noScenario() {
        String expected = "{\"test\":\"test test\"}";
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "ExecutedTestTest.toString() failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_noScenario")
    public void toString_oneScenario() {
        String scenarioDescription = String.format(SCENARIO_DESCRIPTION, "test");
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\"]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenarioDescription);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, scenarioDescription);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "ExecutedTestTest.toString() failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_oneScenario")
    public void toString_multipleScenarios() {
        String firstScenarioDescription = String.format(SCENARIO_DESCRIPTION, "A test");
        String secondScenarioDescription = String.format(SCENARIO_DESCRIPTION, "B test");
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, firstScenarioDescription, secondScenarioDescription);
        List<String> scenarioList = Arrays.asList(firstScenarioDescription, secondScenarioDescription);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, scenarioList);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "ExecutedTestTest.toString() failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_noScenario")
    public void addOneScenario() {
        String scenarioDescription = String.format(SCENARIO_DESCRIPTION, "test");
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\"]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenarioDescription);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION);
        test.addScenario(scenarioDescription);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario");
    }

    @Test
    public void initializeWithScenario() {
        String scenarioDescription = String.format(SCENARIO_DESCRIPTION, "initialization");
        String expected = String.format("{\"test\":\"%s\",\"scenarios\":[\"%s\"]}", TEST_DESCRIPTION, scenarioDescription);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, scenarioDescription);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to initialize with scenario");
    }

    @Test(dependsOnMethods = "initializeWithScenario")
    public void initializeWithScenarioAddScenario() {
        String initialScenario = String.format(SCENARIO_DESCRIPTION, "1");
        String secondScenario = String.format(SCENARIO_DESCRIPTION, "2");
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, initialScenario, secondScenario);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, initialScenario);
        test.addScenario(secondScenario);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add second scenario after initializing with first");
    }

    @Test
    public void initializeWithScenarios() {
        String scenario_1 = String.format(SCENARIO_DESCRIPTION, "first");
        String scenario_2 = String.format(SCENARIO_DESCRIPTION, "second");
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, Arrays.asList(scenario_1, scenario_2));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to initialize with list of scenarios");
    }

    @Test(dependsOnMethods = {"initializeWithScenarios", "initializeWithScenarioAddScenario"})
    public void initializeWithScenariosAddScenario() {
        String scenario_1 = String.format(SCENARIO_DESCRIPTION, "first");
        String scenario_2 = String.format(SCENARIO_DESCRIPTION, "second");
        String scenario_3 = String.format(SCENARIO_DESCRIPTION, "third");
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2, scenario_3);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, Arrays.asList(scenario_1, scenario_2));
        test.addScenario(scenario_3);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to successfully add scenario after initializing with scenario list");
    }

    @Test
    public void addScenarios() {
        String scenario_1 = String.format(SCENARIO_DESCRIPTION, "first");
        String scenario_2 = String.format(SCENARIO_DESCRIPTION, "second");
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION);
        test.addScenarios(Arrays.asList(scenario_1, scenario_2));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list");
    }

    @Test(dependsOnMethods = {"initializeWithScenario"})
    public void initializeWithScenarioAddScenarios() {
        String initialScenario = String.format(SCENARIO_DESCRIPTION, "initializing");
        String scenario_2 = String.format(SCENARIO_DESCRIPTION, "second");
        String scenario_3 = String.format(SCENARIO_DESCRIPTION, "third");
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, initialScenario, scenario_2, scenario_3);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, initialScenario);
        test.addScenarios(Arrays.asList(scenario_2, scenario_3));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"initializeWithScenarios", "addScenarios"})
    public void initializeWithScenariosAddScenarios() {
        String scenario_1 = String.format(SCENARIO_DESCRIPTION, "1");
        String scenario_2 = String.format(SCENARIO_DESCRIPTION, "2");
        String scenario_3 = String.format(SCENARIO_DESCRIPTION, "3");
        String scenario_4 = String.format(SCENARIO_DESCRIPTION, "4");
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\",\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2, scenario_3, scenario_4);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, Arrays.asList(scenario_1, scenario_2));
        test.addScenarios(Arrays.asList(scenario_3, scenario_4));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"addOneScenario", "addScenarios"})
    public void addScenariosAfterAddScenario() {
        String scenario_1 = String.format(SCENARIO_DESCRIPTION, "first");
        String scenario_2 = String.format(SCENARIO_DESCRIPTION, "second");
        String scenario_3 = String.format(SCENARIO_DESCRIPTION, "third");
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2, scenario_3);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION);
        test.addScenario(scenario_1);
        test.addScenarios(Arrays.asList(scenario_2, scenario_3));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"addOneScenario", "addScenarios"})
    public void addScenarioAfterAddScenarios() {
        String scenario_1 = String.format(SCENARIO_DESCRIPTION, "first");
        String scenario_2 = String.format(SCENARIO_DESCRIPTION, "second");
        String scenario_3 = String.format(SCENARIO_DESCRIPTION, "third");
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2, scenario_3);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION);
        test.addScenarios(Arrays.asList(scenario_1, scenario_2));
        test.addScenario(scenario_3);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"addScenarios"})
    public void addScenariosAfterAddScenarios() {
        String scenario_1 = String.format(SCENARIO_DESCRIPTION, "1");
        String scenario_2 = String.format(SCENARIO_DESCRIPTION, "2");
        String scenario_3 = String.format(SCENARIO_DESCRIPTION, "3");
        String scenario_4 = String.format(SCENARIO_DESCRIPTION, "4");
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\",\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2, scenario_3, scenario_4);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION);
        test.addScenarios(Arrays.asList(scenario_1, scenario_2));
        test.addScenarios(Arrays.asList(scenario_3, scenario_4));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"addScenarios"})
    public void toString_scenarioOrder() {
        String scenario_1 = String.format(SCENARIO_DESCRIPTION, "1");
        String scenario_2 = String.format(SCENARIO_DESCRIPTION, "2");
        String scenario_3 = String.format(SCENARIO_DESCRIPTION, "3");
        String scenario_4 = String.format(SCENARIO_DESCRIPTION, "4");
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\",\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2, scenario_3, scenario_4);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, Arrays.asList(scenario_4, scenario_3, scenario_2, scenario_1));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "toString() failed to sort the scenario list alphabetically");
    }

    @Test(dependsOnMethods = {"addScenarios"})
    public void toString_ensureUniqueness() {
        String scenario_1 = String.format(SCENARIO_DESCRIPTION, "A");
        String scenario_2 = String.format(SCENARIO_DESCRIPTION, "B");
        String scenario_3 = String.format(SCENARIO_DESCRIPTION, "B");
        String scenario_4 = String.format(SCENARIO_DESCRIPTION, "A");
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\"]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, Arrays.asList(scenario_4, scenario_3, scenario_2, scenario_1));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "toString() failed to ensure uniqueness of the scenarios");
    }
}
