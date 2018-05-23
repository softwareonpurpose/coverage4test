package com.softwareonpurpose.traceability4test;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

@Test
public class SubjectCoverageTest {
    @Test
    public void toString_json() {
        String testSubject = "test subject";
        String description = "any test";
        String expected = String.format("{\"description\":\"%s\",\"test\":[{\"description\":\"%s\"}]}", testSubject, description);
        String actual = new SubjectCoverage("test subject", ExecutedTest.construct(description)).toString();
        Assert.assertEquals(actual, expected, "Failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_json")
    public void create_withSimpleTest() {
        String testSubject = "test subject";
        String description = "any test";
        String expected = String.format("{\"description\":\"%s\",\"test\":[{\"description\":\"%s\"}]}", testSubject, description);
        String actual = SubjectCoverage.construct(testSubject, ExecutedTest.construct(description)).toString();
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
        SubjectCoverage subject = SubjectCoverage.construct(testSubject, ExecutedTest.construct(test_1));
        subject.addTest(ExecutedTest.construct(test_2));
        String actual = subject.toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }

    @Test(dependsOnMethods = "addTest")
    public void addTests() {
        String testSubject = "test subject";
        String test_1 = "test 1";
        String test_2 = "test 2";
        String test_3 = "test 3";
        List<ExecutedTest> tests = Arrays.asList(ExecutedTest.construct(test_2), ExecutedTest.construct(test_3));
        String expected =
                String.format("{\"description\":\"%s\",\"test\":[{\"description\":\"%s\"},{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testSubject, test_1, test_2, test_3);
        SubjectCoverage subject = SubjectCoverage.construct(testSubject, ExecutedTest.construct(test_1));
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
        List<ExecutedTest> tests = Arrays.asList(ExecutedTest.construct(test_1), ExecutedTest.construct(test_2), ExecutedTest.construct(test_3));
        String expected =
                String.format("{\"description\":\"%s\",\"test\":[{\"description\":\"%s\"},{\"description\":\"%s\"},{\"description\":\"%s\"}]}",
                        testSubject, test_1, test_2, test_3);
        SubjectCoverage subject = SubjectCoverage.construct(testSubject, tests);
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
        String actual = SubjectCoverage.construct(testSubject, ExecutedTest.construct(description, scenarios)).toString();
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
        List<ExecutedTest> tests = Arrays.asList(ExecutedTest.construct(test_a, scenarios), ExecutedTest.construct(test_b, scenarios));
        String expected =
                String.format("{\"description\":\"%s\",\"test\":[{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"}]},{\"description\":\"%s\",\"scenario\":[{\"description\":\"%s\"},{\"description\":\"%s\"}]}]}",
                        testSubject, test_a, scenario_1, scenario_2, test_b, scenario_1, scenario_2);
        String actual = SubjectCoverage.construct(testSubject, tests).toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }
}
