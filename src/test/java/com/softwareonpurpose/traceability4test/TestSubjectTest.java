package com.softwareonpurpose.traceability4test;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

@Test
public class TestSubjectTest {
    @Test
    public void toString_json() {
        String testSubject = "test subject";
        String description = "any test";
        String expected = String.format("{\"description\":\"%s\",\"test\":[{\"description\":\"%s\"}]}", testSubject, description);
        String actual = new TestSubject("test subject", ExecutedTest.create(description)).toString();
        Assert.assertEquals(actual, expected, "Failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_json")
    public void create_withSimpleTest() {
        String testSubject = "test subject";
        String description = "any test";
        String expected = String.format("{\"description\":\"%s\",\"test\":[{\"description\":\"%s\"}]}", testSubject, description);
        String actual = TestSubject.create(testSubject, ExecutedTest.create(description)).toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }

    @Test(dependsOnMethods = "toString_json")
    public void addTest() {
        String testSubject = "test subject";
        String test_1 = "test 1";
        String test_2 = "test 2";
        String expected =
                String.format("{\"description\":\"%s\",\"test\":[{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testSubject, test_1, test_2);
        TestSubject subject = TestSubject.create(testSubject, ExecutedTest.create(test_1));
        subject.addTest(test_2);
        String actual = subject.toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }

    @Test(dependsOnMethods = "addTest")
    public void addTests() {
        String testSubject = "test subject";
        String test_1 = "test 1";
        String test_2 = "test 2";
        String test_3 = "test 3";
        List<ExecutedTest> tests = Arrays.asList(ExecutedTest.create(test_2), ExecutedTest.create(test_3));
        String expected =
                String.format("{\"description\":\"%s\",\"test\":[{\"description\":\"%s\"},{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testSubject, test_1, test_2, test_3);
        TestSubject subject = TestSubject.create(testSubject, ExecutedTest.create(test_1));
        subject.addTests(tests);
        String actual = subject.toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }

    @Test
    public void create_withTests() {
        String testSubject = "test subject";
        String test_1 = "test 1";
        String test_2 = "test 2";
        String test_3 = "test 3";
        List<ExecutedTest> tests = Arrays.asList(ExecutedTest.create(test_1), ExecutedTest.create(test_2), ExecutedTest.create(test_3));
        String expected =
                String.format("{\"description\":\"%s\",\"test\":[{\"description\":\"%s\"},{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testSubject, test_1, test_2, test_3);
        TestSubject subject = TestSubject.create(testSubject, tests);
        String actual = subject.toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }

    @Test(dependsOnMethods = "addTests")
    public void toString_complexTest() {
        String testSubject = "test subject";

        String description = "any test";
        String scenario_1 = "scenario 1";
        String scenario_2 = "scenario 2";
        List<String> scenarios = Arrays.asList(scenario_1, scenario_2);
        String expected =
                String.format("{\"description\":\"%s\",\"test\":[{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"}]}]}",
                        testSubject, description, scenario_1, scenario_2);
        String actual = TestSubject.create(testSubject, ExecutedTest.create(description, scenarios)).toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }

    @Test(dependsOnMethods = "addTests")
    public void toString_multipleComplexTests() {
        String testSubject = "test subject";
        String test_a = "test A";
        String test_b = "test B";
        String scenario_1 = "scenario 1";
        String scenario_2 = "scenario 2";
        List<String> scenarios = Arrays.asList(scenario_1, scenario_2);
        List<ExecutedTest> tests = Arrays.asList(ExecutedTest.create(test_a, scenarios), ExecutedTest.create(test_b, scenarios));
        String expected =
                String.format("{\"description\":\"%s\",\"test\":[{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"}]},{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"}]}]}",
                        testSubject, test_a, scenario_1, scenario_2, test_b, scenario_1, scenario_2);
        String actual = TestSubject.create(testSubject, tests).toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }
}
