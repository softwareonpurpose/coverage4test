package com.softwareonpurpose.coverage4test;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

@Test
public class ExecutedTestTests {
    @Test
    public void testGetScenarios() {
        Scenario scenario_1 = Scenario.getInstance("scenario 1");
        Scenario scenario_2 = Scenario.getInstance("scenario 2");
        Collection<Scenario> expected = new ArrayList<>();
        expected.add(scenario_1);
        expected.add(scenario_2);
        Collection<Scenario> actual = ExecutedTest.getInstance("test", expected).getScenarios();
        Assert.assertEquals(actual, expected, "Failed to return list of Scenarios provided to ExecutedTest");
    }

    @Test
    public void testGetScenarios_sorted() {
        Scenario scenario_1 = Scenario.getInstance("scenario 1");
        Scenario scenario_2 = Scenario.getInstance("scenario 2");
        Collection<Scenario> testScenarios = new ArrayList<>();
        testScenarios.add(scenario_2);
        testScenarios.add(scenario_1);
        SortedSet<Scenario> expected = new TreeSet<>(testScenarios);
        Collection<Scenario> actual = ExecutedTest.getInstance("test", testScenarios).getScenarios();
        String failureMessage = "Failed to return sorted list of Scenarios provided to ExecutedTest";
        Assert.assertEquals(actual, expected, failureMessage);
    }

    @Test
    public void testGetInstance_withScenario() {
        Class expected = ExecutedTest.class;
        Object actual = ExecutedTest.getInstance("test", Scenario.getInstance("scenario")).getClass();
        String failureFormat = "Failed to return an instance of %s when Scenario provided";
        Assert.assertEquals(actual, expected, String.format(failureFormat, expected));
    }

    @Test
    public void testGetInstance_withScenario_null() {
        Class expected = ExecutedTest.class;
        Object actual = ExecutedTest.getInstance("test", (Scenario) null).getClass();
        String failureFormat = "Failed to return an instance of %s when null Scenario provided";
        Assert.assertEquals(actual, expected, String.format(failureFormat, expected));
    }

    @Test
    public void testGetInstance_withScenario_empty() {
        Class expected = ExecutedTest.class;
        Object actual = ExecutedTest.getInstance("test", new ArrayList<>()).getClass();
        String failureFormat = "Failed to return an instance of %s when empty Collection<Scenario> provided";
        Assert.assertEquals(actual, expected, String.format(failureFormat, expected));
    }

    @Test
    public void testAddScenario_null() {
        ExecutedTest test = ExecutedTest.getInstance("test");
        test.addScenario(null);
        Collection<Scenario> actual = test.getScenarios();
        String failureMessage = "Adding <null> scenario initialized scenario list";
        Assert.assertNull(actual, failureMessage);
    }

    @Test
    public void testAddScenario_scenarioListNull() {
        Scenario testScenario = Scenario.getInstance("scenario");
        Collection<Scenario> expected = Collections.singletonList(testScenario);
        ExecutedTest test = ExecutedTest.getInstance("test");
        test.addScenario(testScenario);
        Collection<Scenario> actual = test.getScenarios();
        String failureMessage = "Failed to add Scenario when scenario list null";
        Assert.assertEquals(actual, expected, failureMessage);
    }

    @Test
    public void testAddScenario_scenarioListPopulated() {
        Scenario scenario_1 = Scenario.getInstance("scenario 1");
        Scenario scenario_2 = Scenario.getInstance("scenario 2");
        Collection<Scenario> expected = Arrays.asList(scenario_1, scenario_2);
        ExecutedTest test = ExecutedTest.getInstance("test", scenario_1);
        test.addScenario(scenario_2);
        Collection<Scenario> actual = test.getScenarios();
        String failureMessage = "Failed to add Scenario to populated scenario list";
        Assert.assertEquals(actual, expected, failureMessage);
    }

    @Test
    public void testAddScenarios_nullToNullScenarioList() {
        ExecutedTest test = ExecutedTest.getInstance("test");
        test.addScenarios(null);
        Collection<Scenario> actual = test.getScenarios();
        String failureMessage = "Initialized scenario list when null provided";
        Assert.assertNull(actual, failureMessage);
    }

    @Test
    public void testAddScenarios_nullToPopulatedScenarioList() {
        Scenario scenario = Scenario.getInstance("scenario 1");
        ExecutedTest test = ExecutedTest.getInstance("test", scenario);
        Collection<Scenario> expected = test.getScenarios();
        test.addScenarios(null);
        Collection<Scenario> actual = test.getScenarios();
        String failureMessage = "Added <null> Scenario to scenario list";
        Assert.assertEquals(actual, expected, failureMessage);
    }

    @Test
    public void testAddScenarios_emptyListToNullScenarioList() {
        ExecutedTest test = ExecutedTest.getInstance("test");
        test.addScenarios(new ArrayList<>());
        Collection<Scenario> actual = test.getScenarios();
        String failureMessage = "Initialized scenario list when empty Scenario list provided";
        Assert.assertNull(actual, failureMessage);
    }

    @Test
    public void testAddScenarios_EmptyListToPopulatedScenarioList() {
        Scenario scenario = Scenario.getInstance("scenario 1");
        ExecutedTest test = ExecutedTest.getInstance("test", scenario);
        Collection<Scenario> expected = test.getScenarios();
        test.addScenarios(new ArrayList<>());
        Collection<Scenario> actual = test.getScenarios();
        String failureMessage = "Added Scenario to scenario list when empty Scenario list provided";
        Assert.assertEquals(actual, expected, failureMessage);
    }

    @Test
    public void testAddScenarios_scenarioListToPopulatedScenarioList() {
        Scenario scenario_2 = Scenario.getInstance("scenario 2");
        Collection<Scenario> testList = Collections.singletonList(scenario_2);
        Scenario scenario_1 = Scenario.getInstance("scenario 1");
        Collection<Scenario> expected = Arrays.asList(scenario_1, scenario_2);
        ExecutedTest test = ExecutedTest.getInstance("test", scenario_1);
        test.addScenarios(testList);
        Collection<Scenario> actual = test.getScenarios();
        String failureMessage = "Failed to add Scenarios to populated scenario list";
        Assert.assertEquals(actual, expected, failureMessage);
    }

    @Test
    public void testAddScenarios_scenarioListToNullScenarioList() {
        Scenario scenario_1 = Scenario.getInstance("scenario 1");
        Scenario scenario_2 = Scenario.getInstance("scenario 2");
        Collection<Scenario> expected = Arrays.asList(scenario_1, scenario_2);
        ExecutedTest test = ExecutedTest.getInstance("test");
        test.addScenarios(expected);
        Collection<Scenario> actual = test.getScenarios();
        String failureMessage = "Failed to add Scenarios to null scenario list";
        Assert.assertEquals(actual, expected, failureMessage);
    }

    @Test
    public void testHashCode() {
        List<Scenario> scenarios_actual = Arrays.asList(Scenario.getInstance("scenario_1"), Scenario.getInstance(1));
        List<Scenario> scenarios_expected = Arrays.asList(Scenario.getInstance("scenario_1"), Scenario.getInstance(1));
        int expected = ExecutedTest.getInstance("test", scenarios_actual).hashCode();
        int actual = ExecutedTest.getInstance("test", scenarios_expected).hashCode();
        Assert.assertEquals(actual, expected, "Failed to return same int for two instances with same values");
    }

    @Test
    public void testEquals_null() {
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance("scenario_1"), Scenario.getInstance(1));
        ExecutedTest test = ExecutedTest.getInstance("test", scenarios);
        //noinspection SimplifiedTestNGAssertion,ConstantConditions
        Assert.assertFalse(test.equals(null), "Failed to return 'false' when comparator is null");
    }

    @Test
    public void testEquals_self() {
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance("scenario_1"), Scenario.getInstance(1));
        ExecutedTest test = ExecutedTest.getInstance("test", scenarios);
        //noinspection EqualsWithItself,SimplifiedTestNGAssertion
        Assert.assertTrue(test.equals(test), "Failed to return 'true' when ExecutedTest compared to self");
    }

    @Test
    public void testEquals_differentType() {
        String comparator = "Different Type";
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance("scenario_1"), Scenario.getInstance(1));
        ExecutedTest test = ExecutedTest.getInstance("test", scenarios);
        String message = "Failed to return 'false' when ExecutedTest compared to different Type";
        //noinspection SimplifiedTestNGAssertion,EqualsBetweenInconvertibleTypes
        Assert.assertFalse(test.equals(comparator), message);
    }

    @Test
    public void testEquals_duplicateObjects() {
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance("scenario_1"), Scenario.getInstance(1));
        ExecutedTest test_1 = ExecutedTest.getInstance("test", scenarios);
        ExecutedTest test_2 = ExecutedTest.getInstance("test", scenarios);
        String message = "Failed to return 'true' when comparator is identical to ExecutedTest";
        //noinspection SimplifiedTestNGAssertion
        Assert.assertTrue(test_1.equals(test_2), message);
    }

    @Test
    public void testCompareTo_null() {
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance("scenario_1"), Scenario.getInstance(1));
        ExecutedTest test = ExecutedTest.getInstance("test", scenarios);
        String message = "Failed to return negative int when comparator is null";
        Assert.assertTrue(test.compareTo(null) < 0, message);
    }

    @Test
    public void testCompareTo_self() {
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance("scenario_1"), Scenario.getInstance(1));
        ExecutedTest test = ExecutedTest.getInstance("test", scenarios);
        String message = "Failed to return zero when ExecutedTest compared to self";
        //noinspection EqualsWithItself
        Assert.assertEquals(test.compareTo(test), 0, message);
    }

    @Test
    public void testCompareTo_sameTestDescription() {
        List<Scenario> scenarios_1 = Arrays.asList(Scenario.getInstance("scenario_1"), Scenario.getInstance(1));
        List<Scenario> scenarios_2 = Arrays.asList(Scenario.getInstance("scenario_2"), Scenario.getInstance(2));
        ExecutedTest test_1 = ExecutedTest.getInstance("test", scenarios_1);
        ExecutedTest test_2 = ExecutedTest.getInstance("test", scenarios_2);
        String message = "Failed to return zero when comparator has same test description";
        Assert.assertEquals(test_2.compareTo(test_1), 0, message);
    }

    @Test
    public void testCompareTo_differentTestDescription() {
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance("scenario"), Scenario.getInstance(1));
        ExecutedTest test_1 = ExecutedTest.getInstance("test 1", scenarios);
        ExecutedTest test_2 = ExecutedTest.getInstance("test 2", scenarios);
        String message = "Failed to return non-zero when comparator has different test description";
        Assert.assertNotEquals(test_2.compareTo(test_1), 0, message);
    }

    @Test
    public void testCompareTo_comparatorTestDescriptionNull() {
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance("scenario"), Scenario.getInstance(1));
        ExecutedTest thisTest = ExecutedTest.getInstance("test 1", scenarios);
        ExecutedTest comparatorTest = ExecutedTest.getInstance(null, scenarios);
        String message = "Failed to return non-zero when comparator has null test description";
        Assert.assertNotEquals(thisTest.compareTo(comparatorTest), 0, message);
    }

    @Test
    public void testCompareTo_thisTestDescriptionNull() {
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance("scenario"), Scenario.getInstance(1));
        ExecutedTest thisTest = ExecutedTest.getInstance(null, scenarios);
        ExecutedTest comparatorTest = ExecutedTest.getInstance("test 2", scenarios);
        String message = "Failed to return non-zero when ExecutedTest has null test description";
        Assert.assertNotEquals(thisTest.compareTo(comparatorTest), 0, message);
    }

    @Test
    public void testCompareTo_bothTestDescriptionsNull() {
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance("scenario"), Scenario.getInstance(1));
        ExecutedTest thisTest = ExecutedTest.getInstance(null, scenarios);
        ExecutedTest comparatorTest = ExecutedTest.getInstance(null, scenarios);
        String message = "Failed to return zero when both test descriptions null";
        Assert.assertEquals(thisTest.compareTo(comparatorTest), 0, message);
    }

    @Test
    public void testGetScenarioCount() {
        int expected = 3;
        Scenario scenario_1 = Scenario.getInstance("scenario");
        Scenario scenario_2 = Scenario.getInstance(false);
        Scenario scenario_3 = Scenario.getInstance(999);
        List<Scenario> scenarios = Arrays.asList(scenario_1, scenario_2, scenario_3);
        ExecutedTest test = ExecutedTest.getInstance("test", scenarios);
        int actual = test.getScenarioCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of scenarios");
    }

    @Test
    public void testGetScenarioCount_emptyScenarioList() {
        int expected = 0;
        ExecutedTest test = ExecutedTest.getInstance("test");
        int actual = test.getScenarioCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of scenarios");
    }
}
