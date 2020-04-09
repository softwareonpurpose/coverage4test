package com.softwareonpurpose.coverage4test;

import com.softwareonpurpose.coverage4test.mock.ScenarioObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Test
public class SubjectCoverageTestReplace {
    private static final String TEST_SUBJECT = "subject";
    private static final String ANY_TEST = "any test";
    private static final String FAILURE_MESSAGE = "Failed to return expected json";
    private static final String TEST_DESCRIPTION = "test %s";
    private static final String SCENARIO_DESCRIPTION = "scenario %d";


    @Test
    public void toString_json() {
        String formattedSubject = formatSubject();
        String formattedTest = formatTest();
        String formattedTests = String.format("  \"tests\": [\n%s\n  ]", formattedTest);
        String expected = formatReport(formattedSubject, formattedTests);

        String actual = SubjectCoverage.getInstance(TEST_SUBJECT, ExecutedTest.getInstance(ANY_TEST)).toString();

        Assert.assertEquals(actual, expected, FAILURE_MESSAGE);
    }

    @Test(dependsOnMethods = "toString_json")
    public void addTest() {
        String test_1 = String.format(TEST_DESCRIPTION, "1");
        String test_2 = String.format(TEST_DESCRIPTION, "2");
        List<String> testList = new ArrayList<>();
        testList.add(test_1);
        testList.add(test_2);
        String formattedSubject = formatSubject();
        String formattedTests = formatTests(testList);
        String expected = formatReport(formattedSubject, formattedTests);

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
        List<String> testList = new ArrayList<>();
        testList.add(test_1);
        testList.add(test_2);
        testList.add(test_3);
        String formattedSubject = formatSubject();
        String formattedTests = formatTests(testList);
        String expected = formatReport(formattedSubject, formattedTests);

        List<ExecutedTest> tests = Arrays.asList(ExecutedTest.getInstance(test_2), ExecutedTest.getInstance(test_3));
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
        List<String> testList = new ArrayList<>();
        testList.add(test_1);
        testList.add(test_2);
        testList.add(test_3);
        String formattedSubject = formatSubject();
        String formattedTests = formatTests(testList);
        String expected = formatReport(formattedSubject, formattedTests);

        List<ExecutedTest> tests =
                Arrays.asList(ExecutedTest.getInstance(test_1), ExecutedTest.getInstance(test_2), ExecutedTest.getInstance(test_3));
        SubjectCoverage subject = SubjectCoverage.getInstance(TEST_SUBJECT, tests);
        String actual = subject.toString();

        Assert.assertEquals(actual, expected, "toString() " + FAILURE_MESSAGE);
    }

    @Test(dependsOnMethods = "addTests")
    public void toString_complexTest() {
        Scenario scenario_1 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, 1));
        Scenario scenario_2 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, 2));
        List<Scenario> scenarios = Arrays.asList(scenario_1, scenario_2);
        String expected = "{\n" +
                "  \"subject\": \"subject\",\n" +
                "  \"tests\": [\n" +
                "    {\n" +
                "      \"test\": \"any test\",\n" +
                "      \"scenarios\": [\n" +
                "        {\n" +
                "          \"scenario\": \"scenario 1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"scenario\": \"scenario 2\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        String actual = SubjectCoverage.getInstance(TEST_SUBJECT, ExecutedTest.getInstance(ANY_TEST, scenarios)).toString();

        Assert.assertEquals(actual, expected, "toString() " + FAILURE_MESSAGE);
    }

    @Test(dependsOnMethods = "addTests")
    public void toString_complexScenarios() {
        Scenario scenario = Scenario.getInstance(ScenarioObject.getInstance("text value", 9, false));
        List<Scenario> scenarios = Collections.singletonList(scenario);
        String expected = "{\n" +
                "  \"subject\": \"subject\",\n" +
                "  \"tests\": [\n" +
                "    {\n" +
                "      \"test\": \"any test\",\n" +
                "      \"scenarios\": [\n" +
                "        {\n" +
                "          \"scenario\": {\n" +
                "            \"text\": \"text value\",\n" +
                "            \"integer\": 9,\n" +
                "            \"aBoolean\": false\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        String actual = SubjectCoverage.getInstance(TEST_SUBJECT, ExecutedTest.getInstance(ANY_TEST, scenarios)).toString();

        Assert.assertEquals(actual, expected, "toString() " + FAILURE_MESSAGE);
    }

    @Test(dependsOnMethods = "addTests")
    public void toString_multipleComplexTests() {
        String test_a = String.format(TEST_DESCRIPTION, "A");
        String test_b = String.format(TEST_DESCRIPTION, "B");
        Scenario scenario_1 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, 1));
        Scenario scenario_2 = Scenario.getInstance(String.format(SCENARIO_DESCRIPTION, 2));
        List<Scenario> scenarios = Arrays.asList(scenario_1, scenario_2);
        List<ExecutedTest> tests = Arrays.asList(ExecutedTest.getInstance(test_a, scenarios), ExecutedTest.getInstance(test_b, scenarios));
        String expected = "{\n" +
                "  \"subject\": \"subject\",\n" +
                "  \"tests\": [\n" +
                "    {\n" +
                "      \"test\": \"test A\",\n" +
                "      \"scenarios\": [\n" +
                "        {\n" +
                "          \"scenario\": \"scenario 1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"scenario\": \"scenario 2\"\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    {\n" +
                "      \"test\": \"test B\",\n" +
                "      \"scenarios\": [\n" +
                "        {\n" +
                "          \"scenario\": \"scenario 1\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"scenario\": \"scenario 2\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        String actual = SubjectCoverage.getInstance(TEST_SUBJECT, tests).toString();

        Assert.assertEquals(actual, expected, "toString() " + FAILURE_MESSAGE);
    }

    private String formatReport(String formattedSubject, String formattedTests) {
        return String.format("{\n%s,\n%s\n}", formattedSubject, formattedTests);
    }

    private String formatTests(List<String> tests) {
        StringBuilder formattedTests = new StringBuilder("  \"tests\": [");
        for (String test : tests) {
            formattedTests.append(String.format("\n    {\n      \"test\": \"%s\"\n    },", test));
        }
        if (formattedTests.lastIndexOf(",") == formattedTests.length() - 1) {
            formattedTests.deleteCharAt(formattedTests.length() - 1);
        }
        formattedTests.append("\n  ]");
        return formattedTests.toString();
    }

    private String formatTest() {
        return String.format("    {\n      \"test\": \"%s\"\n    }", ANY_TEST);
    }

    private String formatSubject() {
        return String.format("  \"subject\": \"%s\"", TEST_SUBJECT);
    }
}
