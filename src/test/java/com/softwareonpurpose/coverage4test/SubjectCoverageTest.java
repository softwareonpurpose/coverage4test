package com.softwareonpurpose.coverage4test;

import com.google.gson.Gson;
import com.softwareonpurpose.coverage4test.mock.ScenarioObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Test
public class SubjectCoverageTest {
    private static final String TEST_SUBJECT = "subject";
    private static final String ANY_TEST = "any test";
    private static final String FAILURE_MESSAGE = "Failed to return expected json";
    private static final String TEST_DESCRIPTION = "test %s";
    private static final String SCENARIO_DESCRIPTION = "scenario %d";
    private static final ScenarioObject scenario = ScenarioObject.getInstance("Text Value", 9, false);
    @Test
    public void toString_json() {
        String expectedFormat = "{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"}]}";
        String expected = String.format(expectedFormat, TEST_SUBJECT, ANY_TEST);
        String actual = SubjectCoverage.getInstance(TEST_SUBJECT, ExecutedTest.getInstance(ANY_TEST)).toString();
        Assert.assertEquals(actual, expected, FAILURE_MESSAGE);
    }

    @Test(dependsOnMethods = "toString_json")
    public void create_withSimpleTest() {
        String expected = String.format("{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"}]}", TEST_SUBJECT, ANY_TEST);
        String actual = SubjectCoverage.getInstance(TEST_SUBJECT, ExecutedTest.getInstance(ANY_TEST)).toString();
        Assert.assertEquals(actual, expected, "toString() " + FAILURE_MESSAGE);
    }

    @Test(dependsOnMethods = "toString_json")
    public void addTest() {
        String test_1 = String.format(TEST_DESCRIPTION, "1");
        String test_2 = String.format(TEST_DESCRIPTION, "2");
        String expected =
                String.format("{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"},{\"test\":\"%s\"}]}",
                        TEST_SUBJECT, test_1, test_2);
        SubjectCoverage subject = SubjectCoverage.getInstance(TEST_SUBJECT, ExecutedTest.getInstance(test_1));
        subject.addTest(ExecutedTest.getInstance(test_2));
        String actual = subject.toString();
        Assert.assertEquals(actual, expected, "toString() " + FAILURE_MESSAGE);
    }

    @Test(dependsOnMethods = "addTest")
    public void addTests() {
        String test_1 = String.format(TEST_DESCRIPTION, "1");
        String test_2 = String.format(TEST_DESCRIPTION, "2");
        String test_3 = String.format(TEST_DESCRIPTION, "3");
        List<ExecutedTest> tests = Arrays.asList(ExecutedTest.getInstance(test_2), ExecutedTest.getInstance(test_3));
        String expected =
                String.format("{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"},{\"test\":\"%s\"},{\"test\":\"%s\"}]}",
                        TEST_SUBJECT, test_1, test_2, test_3);
        SubjectCoverage subject = SubjectCoverage.getInstance(TEST_SUBJECT, ExecutedTest.getInstance(test_1));
        subject.addTests(tests);
        String actual = subject.toString();
        Assert.assertEquals(actual, expected, "toString() " + FAILURE_MESSAGE);
    }

    @Test
    public void create_withTests() {
        String test_1 = String.format(TEST_DESCRIPTION, "1");
        String test_2 = String.format(TEST_DESCRIPTION, "2");
        String test_3 = String.format(TEST_DESCRIPTION, "3");
        List<ExecutedTest> tests =
                Arrays.asList(ExecutedTest.getInstance(test_1), ExecutedTest.getInstance(test_2), ExecutedTest.getInstance(test_3));
        String expectedFormat =
                "{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"},{\"test\":\"%s\"},{\"test\":\"%s\"}]}";
        String expected = String.format(expectedFormat, TEST_SUBJECT, test_1, test_2, test_3);
        SubjectCoverage subject = SubjectCoverage.getInstance(TEST_SUBJECT, tests);
        String actual = subject.toString();
        Assert.assertEquals(actual, expected, "toString() " + FAILURE_MESSAGE);
    }

    @Test(dependsOnMethods = "addTests")
    public void toString_complexTest() {
        String scenario_1 = String.format(SCENARIO_DESCRIPTION, 1);
        String scenario_2 = String.format(SCENARIO_DESCRIPTION, 2);
        List<String> scenarios = Arrays.asList(scenario_1, scenario_2);
        String expectedFormat = "{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\"]}]}";
        String expected = String.format(expectedFormat, TEST_SUBJECT, ANY_TEST, scenario_1, scenario_2);
        String actual = SubjectCoverage.getInstance(TEST_SUBJECT, ExecutedTest.getInstance(ANY_TEST, scenarios)).toString();
        Assert.assertEquals(actual, expected, "toString() " + FAILURE_MESSAGE);
    }

    @Test(dependsOnMethods = "addTests")
    public void toString_complexScenarios() {
        ScenarioObject scenario = ScenarioObject.getInstance("text value", 9, false);
        String scenarioDescription = new Gson().toJson(scenario);
        List<String> scenarios = Collections.singletonList(scenarioDescription);
        String expectedFormat = "{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\",\"scenarios\":[\"%s\"]}]}";
        String expected = String.format(expectedFormat, TEST_SUBJECT, ANY_TEST, scenarioDescription);
        String actual = SubjectCoverage.getInstance(TEST_SUBJECT, ExecutedTest.getInstance(ANY_TEST, scenarios)).toString();
        Assert.assertEquals(actual, expected, "toString() " + FAILURE_MESSAGE);
    }

    @Test(dependsOnMethods = "addTests")
    public void toString_multipleComplexTests() {
        String test_a = String.format(TEST_DESCRIPTION, "A");
        String test_b = String.format(TEST_DESCRIPTION, "B");
        String scenario_1 = String.format(SCENARIO_DESCRIPTION, 1);
        String scenario_2 = String.format(SCENARIO_DESCRIPTION, 2);
        List<String> scenarios = Arrays.asList(scenario_1, scenario_2);
        List<ExecutedTest> tests = Arrays.asList(ExecutedTest.getInstance(test_a, scenarios), ExecutedTest.getInstance(test_b, scenarios));
        String expectedFormat =
                "{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\"]},{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\"]}]}";
        String expected = String.format(expectedFormat, TEST_SUBJECT, test_a, scenario_1, scenario_2, test_b, scenario_1, scenario_2);
        String actual = SubjectCoverage.getInstance(TEST_SUBJECT, tests).toString();
        Assert.assertEquals(actual, expected, "toString() " + FAILURE_MESSAGE);
    }
}
