package com.softwareonpurpose.coverage4test;

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
        String expectedFormat = "{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"}]}";
        String expected = String.format(expectedFormat, testSubject, description);
        String actual = SubjectCoverage.getInstance("test subject", ExecutedTest.getInstance(description)).toString();
        Assert.assertEquals(actual, expected, "Failed to return expected json");
    }

    @Test(dependsOnMethods = "toString_json")
    public void create_withSimpleTest() {
        String testSubject = "test subject";
        String description = "any test";
        String expected = String.format("{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"}]}", testSubject, description);
        String actual = SubjectCoverage.getInstance(testSubject, ExecutedTest.getInstance(description)).toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }

    @Test(dependsOnMethods = "toString_json")
    public void addTest() {
        String testSubject = "test subject";
        String test_1 = "test 1";
        String test_2 = "test 2";
        String expected =
                String.format("{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"},{\"test\":\"%s\"}]}",
                        testSubject, test_1, test_2);
        SubjectCoverage subject = SubjectCoverage.getInstance(testSubject, ExecutedTest.getInstance(test_1));
        subject.addTest(ExecutedTest.getInstance(test_2));
        String actual = subject.toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }

    @Test(dependsOnMethods = "addTest")
    public void addTests() {
        String testSubject = "test subject";
        String test_1 = "test 1";
        String test_2 = "test 2";
        String test_3 = "test 3";
        List<ExecutedTest> tests = Arrays.asList(ExecutedTest.getInstance(test_2), ExecutedTest.getInstance(test_3));
        String expected =
                String.format("{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"},{\"test\":\"%s\"},{\"test\":\"%s\"}]}",
                        testSubject, test_1, test_2, test_3);
        SubjectCoverage subject = SubjectCoverage.getInstance(testSubject, ExecutedTest.getInstance(test_1));
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
        List<ExecutedTest> tests =
                Arrays.asList(ExecutedTest.getInstance(test_1), ExecutedTest.getInstance(test_2), ExecutedTest.getInstance(test_3));
        String expectedFormat =
                "{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\"},{\"test\":\"%s\"},{\"test\":\"%s\"}]}";
        String expected = String.format(expectedFormat, testSubject, test_1, test_2, test_3);
        SubjectCoverage subject = SubjectCoverage.getInstance(testSubject, tests);
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
        String expectedFormat = "{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\"]}]}";
        String expected = String.format(expectedFormat, testSubject, description, scenario_1, scenario_2);
        String actual = SubjectCoverage.getInstance(testSubject, ExecutedTest.getInstance(description, scenarios)).toString();
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
        List<ExecutedTest> tests = Arrays.asList(ExecutedTest.getInstance(test_a, scenarios), ExecutedTest.getInstance(test_b, scenarios));
        String expectedFormat =
                "{\"subject\":\"%s\",\"tests\":[{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\"]},{\"test\":\"%s\",\"scenarios\":[\"%s\",\"%s\"]}]}";
        String expected = String.format(expectedFormat, testSubject, test_a, scenario_1, scenario_2, test_b, scenario_1, scenario_2);
        String actual = SubjectCoverage.getInstance(testSubject, tests).toString();
        Assert.assertEquals(actual, expected, "toString() failed to return expected json content");
    }
}
