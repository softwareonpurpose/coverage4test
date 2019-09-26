package com.softwareonpurpose.coverage4test;

import com.softwareonpurpose.coverage4test.mock.ScenarioObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

@Test
public class ExecutedTestTest {
    private static final String TEST_DESCRIPTION = "test";
    private static final String SCENARIO_DESCRIPTION = "scenario %s";

    @Test
    public void toString_noScenario() {
        String expected = "{\"test\":\"test\"}";
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "ExecutedTestTest.toString() failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_noScenario")
    public void toString_oneScenario() {
        Scenario scenario = Scenario.getInstance(ScenarioObject.getInstance("text value", null, null));
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[%s]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario.toString());
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, scenario);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "ExecutedTestTest.toString() failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_oneScenario")
    public void toString_multipleScenarios() {
        Scenario firstScenario = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "A test"));
        Scenario secondScenario = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "B test"));
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[%s,%s]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, firstScenario, secondScenario);
        List<Scenario> scenarioList = Arrays.asList(firstScenario, secondScenario);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, scenarioList);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "ExecutedTestTest.toString() failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_noScenario")
    public void addOneScenario() {
        Scenario scenario = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "test"));
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[%s]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario.toString());
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION);
        test.addScenario(scenario);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario");
    }

    @Test
    public void initializeWithScenario() {
        Scenario scenario = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "initialization"));
        String expected = String.format("{\"test\":\"%s\",\"scenarios\":[%s]}", TEST_DESCRIPTION, scenario);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, scenario);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to initialize with scenario");
    }

    @Test(dependsOnMethods = "initializeWithScenario")
    public void initializeWithScenarioAddScenario() {
        Scenario initialScenario = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "1"));
        Scenario secondScenario = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "2"));
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[%s,%s]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, initialScenario, secondScenario);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, initialScenario);
        test.addScenario(secondScenario);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add second scenario after initializing with first");
    }

    @Test
    public void initializeWithScenarios() {
        Scenario scenario_1 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "first"));
        Scenario scenario_2 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "second"));
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[%s,%s]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, Arrays.asList(scenario_1, scenario_2));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to initialize with list of scenarios");
    }

    @Test(dependsOnMethods = {"initializeWithScenarios", "initializeWithScenarioAddScenario"})
    public void initializeWithScenariosAddScenario() {
        Scenario scenario_1 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "first"));
        Scenario scenario_2 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "second"));
        Scenario scenario_3 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "third"));
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[%s,%s,%s]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2, scenario_3);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, Arrays.asList(scenario_1, scenario_2));
        test.addScenario(scenario_3);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to successfully add scenario after initializing with scenario list");
    }

    @Test
    public void addScenarios() {
        Scenario scenario_1 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "first"));
        Scenario scenario_2 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "second"));
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[%s,%s]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION);
        test.addScenarios(Arrays.asList(scenario_1, scenario_2));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list");
    }

    @Test(dependsOnMethods = {"initializeWithScenario"})
    public void initializeWithScenarioAddScenarios() {
        Scenario initialScenario = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "initializing"));
        Scenario scenario_2 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "second"));
        Scenario scenario_3 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "third"));
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[%s,%s,%s]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, initialScenario, scenario_2, scenario_3);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, initialScenario);
        test.addScenarios(Arrays.asList(scenario_2, scenario_3));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"initializeWithScenarios", "addScenarios"})
    public void initializeWithScenariosAddScenarios() {
        Scenario scenario_1 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "1"));
        Scenario scenario_2 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "2"));
        Scenario scenario_3 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "3"));
        Scenario scenario_4 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "4"));
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[%s,%s,%s,%s]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2, scenario_3, scenario_4);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, Arrays.asList(scenario_1, scenario_2));
        test.addScenarios(Arrays.asList(scenario_3, scenario_4));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"addOneScenario", "addScenarios"})
    public void addScenariosAfterAddScenario() {
        Scenario scenario_1 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "first"));
        Scenario scenario_2 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "second"));
        Scenario scenario_3 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "third"));
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[%s,%s,%s]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2, scenario_3);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION);
        test.addScenario(scenario_1);
        test.addScenarios(Arrays.asList(scenario_2, scenario_3));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"addOneScenario", "addScenarios"})
    public void addScenarioAfterAddScenarios() {
        Scenario scenario_1 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "first"));
        Scenario scenario_2 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "second"));
        Scenario scenario_3 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "third"));
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[%s,%s,%s]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2, scenario_3);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION);
        test.addScenarios(Arrays.asList(scenario_1, scenario_2));
        test.addScenario(scenario_3);
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"addScenarios"})
    public void addScenariosAfterAddScenarios() {
        Scenario scenario_1 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "1"));
        Scenario scenario_2 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "2"));
        Scenario scenario_3 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "3"));
        Scenario scenario_4 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "4"));
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[%s,%s,%s,%s]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2, scenario_3, scenario_4);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION);
        test.addScenarios(Arrays.asList(scenario_1, scenario_2));
        test.addScenarios(Arrays.asList(scenario_3, scenario_4));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "Failed to add scenario list after initializing with first scenario");
    }

    @Test(dependsOnMethods = {"addScenarios"})
    public void toString_scenarioOrder() {
        Scenario scenario_1 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "1"));
        Scenario scenario_2 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "2"));
        Scenario scenario_3 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "3"));
        Scenario scenario_4 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "4"));
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[%s,%s,%s,%s]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2, scenario_3, scenario_4);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, Arrays.asList(scenario_4, scenario_3, scenario_2, scenario_1));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "toString() failed to sort the scenario list alphabetically");
    }

    @Test(dependsOnMethods = {"addScenarios"})
    public void toString_ensureUniqueness() {
        Scenario scenario_1 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "A"));
        Scenario  scenario_2 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "B"));
        Scenario  scenario_3 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "B"));
        Scenario  scenario_4 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, "A"));
        String expectedFormat = "{\"test\":\"%s\",\"scenarios\":[%s,%s]}";
        String expected = String.format(expectedFormat, TEST_DESCRIPTION, scenario_1, scenario_2);
        ExecutedTest test = ExecutedTest.getInstance(TEST_DESCRIPTION, Arrays.asList(scenario_4, scenario_3, scenario_2, scenario_1));
        String actual = test.toString();
        Assert.assertEquals(actual, expected, "toString() failed to ensure uniqueness of the scenarios");
    }
}
