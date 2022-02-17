package com.softwareonpurpose.coverage4test;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

@Test
public class ExecutedTestTests {
    @Test
    public void testGetScenario() {
        Scenario scenario_1 = Scenario.getInstance("scenario 1");
        Collection<Scenario> expected = new ArrayList<>();
        expected.add(scenario_1);
        Collection<Scenario> actual = ExecutedTest.getInstance("test 1", "feature 1", "scenario 1", 1).getScenarios();
        Assert.assertEquals(actual, expected, "Failed to return list of Scenarios provided to ExecutedTest");
    }


    @Test
    public void testGetInstance_withScenario() {
        Class expected = ExecutedTest.class;
        Object actual = ExecutedTest.getInstance("test 1", "feature 1", "scenario 1", 1).getClass();
        String failureFormat = "Failed to return an instance of %s when Scenario provided";
        Assert.assertEquals(actual, expected, String.format(failureFormat, expected));
    }

    @Test
    public void testAddScenario_scenarioListPopulated() {
        Scenario scenario_1 = Scenario.getInstance("scenario 1");
        Scenario scenario_2 = Scenario.getInstance("scenario 2");
        SortedSet expected = new TreeSet<>();
        expected.add(scenario_1);
        expected.add(scenario_2);
        ExecutedTest test = ExecutedTest.getInstance("test 1", "feature 1", "scenario 1", 1);
        test.addScenario(scenario_2);
        Collection<Scenario> actual = test.getScenarios();
        String failureMessage = "Failed to add Scenario to populated scenario list";
        var comparation = actual.equals(expected);
        Assert.assertTrue(comparation, failureMessage);
    }

    @Test
    public void testAddScenarios_scenarioListToPopulatedScenarioList() {
        Scenario scenario_2 = Scenario.getInstance("scenario 2");
        Collection<Scenario> testList = Collections.singletonList(scenario_2);
        Scenario scenario_1 = Scenario.getInstance("scenario 1");
        SortedSet<Scenario> expected = new TreeSet<>();
        expected.add(scenario_1);
        expected.add(scenario_2);
        ExecutedTest test = ExecutedTest.getInstance("test 1", "feature 1", "scenario 1", 1);
        test.addScenarios(testList);
        Collection<Scenario> actual = test.getScenarios();
        String failureMessage = "Failed to add Scenarios to populated scenario list";
        var comparation = actual.equals(expected);
        Assert.assertTrue(comparation, failureMessage);
    }


    @Test
    public void testEquals_self() {
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance("scenario 1"), Scenario.getInstance(1));
        ExecutedTest test = ExecutedTest.getInstance("test 1", "feature 1", "scenario 1", 1);
        //noinspection EqualsWithItself,SimplifiedTestNGAssertion
        Assert.assertTrue(test.equals(test), "Failed to return 'true' when ExecutedTest compared to self");
    }

    @Test
    public void testEquals_differentType() {
        String comparator = "Different Type";
        ExecutedTest test = ExecutedTest.getInstance("test 1", "feature 1", "scenario 1", 1);
        String message = "Failed to return 'false' when ExecutedTest compared to different Type";
        Assert.assertFalse(test.equals(comparator), message);
    }

    @Test
    public void testEquals_duplicateObjects() {
        ExecutedTest test_1 = ExecutedTest.getInstance("test 1", "feature 1", "scenario 1", 1);
        ExecutedTest test_2 = ExecutedTest.getInstance("test 1", "feature 1", "scenario 1", 1);
        String message = "Failed to return 'true' when comparator is identical to ExecutedTest";
        //noinspection SimplifiedTestNGAssertion
        Assert.assertTrue(test_1.equals(test_2), message);
    }

    @Test
    public void testCompareTo_self() {
        ExecutedTest test = ExecutedTest.getInstance("test 1", "feature 1", "scenario 1", 1);
        String message = "Failed to return zero when ExecutedTest compared to self";
        //noinspection EqualsWithItself
        Assert.assertEquals(test.compareTo(test), 0, message);
    }

    @Test
    public void testCompareTo_sameTestName() {
        ExecutedTest test_1 = ExecutedTest.getInstance("test", "feature 1", "scenario 1", 1);
        ExecutedTest test_2 = ExecutedTest.getInstance("test", "feature 1", "scenario 1", 1);
        String message = "Failed to return zero when comparator has same test description";
        Assert.assertEquals(test_2.compareTo(test_1), 0, message);
    }

    @Test
    public void testCompareTo_differentTestName() {
        ExecutedTest test_1 = ExecutedTest.getInstance("test 1", "feature 1", "scenario 1", 1);
        ExecutedTest test_2 = ExecutedTest.getInstance("test 2", "feature 1", "scenario 1", 1);
        String message = "Failed to return non-zero when comparator has different test description";
        Assert.assertNotEquals(test_2.compareTo(test_1), 0, message);
    }

    @Test
    public void testGetScenarioCount() {
        int expected = 1;
        ExecutedTest test = ExecutedTest.getInstance("test 1", "feature 1", "scenario 1", 1);
        int actual = test.getScenarioCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of scenarios");
    }
}
