package com.softwareonpurpose.coverage4test;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Test
public class TestedSubjectTests {
    @Test
    public void testSetVerifications() {
        long expected = 2;
        TestedSubject subject = TestedSubject.getInstance("subject");
        subject.setVerificationCount(expected);
        long actual = subject.getVerificationCount();
        Assert.assertEquals(actual, expected, "Failed to set verification count");
    }

    @Test
    public void testMerge() {
        ExecutedTest test_1 = ExecutedTest.getInstance("test 1");
        ExecutedTest test_2 = ExecutedTest.getInstance("test 2");
        TestedSubject subject_1 = TestedSubject.getInstance("subject", test_1);
        TestedSubject subject_2 = TestedSubject.getInstance("subject", test_2);
        Collection<ExecutedTest> expected = Arrays.asList(test_1, test_2);
        subject_1.merge(subject_2);
        Collection<ExecutedTest> actual = subject_1.getTests();
        Assert.assertEquals(actual, expected, "Failed to merge tests");
    }

    @Test
    public void testMerge_sameTestDifferentScenarios() {
        Scenario scenario_1 = Scenario.getInstance(999);
        Scenario scenario_2 = Scenario.getInstance("scenario 2");
        ExecutedTest test_1a = ExecutedTest.getInstance("test 1", scenario_1);
        ExecutedTest test_1b = ExecutedTest.getInstance("test 1", scenario_2);
        TestedSubject subject_1a = TestedSubject.getInstance("subject", test_1a);
        TestedSubject subject_1b = TestedSubject.getInstance("subject", test_1b);
        Collection<ExecutedTest> expected = Collections.singletonList(test_1a);
        subject_1a.merge(subject_1b);
        Collection<ExecutedTest> actual = subject_1a.getTests();
        Assert.assertEquals(actual, expected, "Failed to merge tests");
    }

    @Test
    public void testGetInstance_tests() {
        ExecutedTest test_1 = ExecutedTest.getInstance("test 1");
        ExecutedTest test_2 = ExecutedTest.getInstance("test 2");
        Collection<ExecutedTest> expectedTests = Arrays.asList(test_1, test_2);
        TestedSubject actual = TestedSubject.getInstance("subject 1", expectedTests);
        Collection<ExecutedTest> actualTests = actual.getTests();
        String message = "Failed to return instantiated SystemRequirement";
        Assert.assertEquals(actual.getClass(), TestedSubject.class, message);
        Assert.assertEquals(actualTests, expectedTests, "Failed to initialize list of tests");
    }

    @Test
    public void testCompareTo_null() {
        ExecutedTest test = ExecutedTest.getInstance("test");
        TestedSubject subject = TestedSubject.getInstance("subject", test);
        String message = "Failed to return negative int when comparator is null";
        Assert.assertTrue(subject.compareTo(null) < 0, message);
    }

    @Test
    public void testCompareTo_bothDescriptionsNull() {
        ExecutedTest test_1 = ExecutedTest.getInstance("test 1");
        ExecutedTest test_2 = ExecutedTest.getInstance("test 2");
        TestedSubject subject_1 = TestedSubject.getInstance(null, test_1);
        TestedSubject subject_2 = TestedSubject.getInstance(null, test_2);
        String message = "Failed to return zero int when both descriptions are null";
        //noinspection SimplifiedTestNGAssertion
        Assert.assertTrue(subject_1.compareTo(subject_2) == 0, message);
    }

    @Test
    public void testCompareTo_comparatorDescriptionNull() {
        ExecutedTest test_1 = ExecutedTest.getInstance("test 1");
        ExecutedTest test_2 = ExecutedTest.getInstance("test 2");
        TestedSubject subject_1 = TestedSubject.getInstance("test 1", test_1);
        TestedSubject subject_2 = TestedSubject.getInstance(null, test_2);
        String message = "Failed to return non-zero int when comparator description is null";
        //noinspection SimplifiedTestNGAssertion
        Assert.assertFalse(subject_1.compareTo(subject_2) == 0, message);
    }

    @Test
    public void testCompareTo_selfDescriptionNull() {
        ExecutedTest test_1 = ExecutedTest.getInstance("test 1");
        ExecutedTest test_2 = ExecutedTest.getInstance("test 2");
        TestedSubject subject_1 = TestedSubject.getInstance("test 1", test_1);
        TestedSubject subject_2 = TestedSubject.getInstance(null, test_2);
        String message = "Failed to return non-zero int when self description is null";
        //noinspection SimplifiedTestNGAssertion
        Assert.assertFalse(subject_2.compareTo(subject_1) == 0, message);
    }

    @Test
    public void testCompareTo_self() {
        ExecutedTest test = ExecutedTest.getInstance("test");
        TestedSubject subject = TestedSubject.getInstance("subject", test);
        String message = "Failed to return 0 int when comparator is self";
        //noinspection EqualsWithItself,SimplifiedTestNGAssertion
        Assert.assertTrue(subject.compareTo(subject) == 0, message);
    }

    @Test
    public void testAddTest() {
        ExecutedTest test = ExecutedTest.getInstance("test 1");
        ExecutedTest duplicateTest = ExecutedTest.getInstance("test 2");
        int expected = 2;
        TestedSubject subject = TestedSubject.getInstance("test", test);
        subject.addTest(duplicateTest);
        int actual = subject.getTests().size();
        String message = "Failed to add new test";
        Assert.assertEquals(actual, expected, message);
    }

    @Test
    public void testAddTest_duplicateTestDescriptionsOnly() {
        ExecutedTest test = ExecutedTest.getInstance("test 1");
        ExecutedTest duplicateTest = ExecutedTest.getInstance("test 1");
        int expected = 1;
        TestedSubject subject = TestedSubject.getInstance("test", test);
        subject.addTest(duplicateTest);
        int actual = subject.getTests().size();
        String message = "Failure:  added duplicate test rather than maintaining uniqueness of tests";
        Assert.assertEquals(actual, expected, message);
    }

    @Test
    public void testAddTest_duplicateTestsWithScenarios() {
        ExecutedTest test = ExecutedTest.getInstance("test", Scenario.getInstance("scenario"));
        ExecutedTest duplicateTest = ExecutedTest.getInstance("test", Scenario.getInstance("scenario"));
        int expected = 1;
        TestedSubject subject = TestedSubject.getInstance("test", test);
        subject.addTest(duplicateTest);
        int actual = subject.getTests().size();
        String message = "Failure:  added duplicate test rather than maintaining uniqueness of tests";
        Assert.assertEquals(actual, expected, message);
    }

    @Test
    public void testAddTest_sameTestsDifferentScenarios() {
        ExecutedTest test = ExecutedTest.getInstance("test", Scenario.getInstance("scenario 1"));
        ExecutedTest duplicateTest = ExecutedTest.getInstance("test", Scenario.getInstance("scenario 2"));
        int expected = 1;
        TestedSubject subject = TestedSubject.getInstance("test", test);
        subject.addTest(duplicateTest);
        int actual = subject.getTests().size();
        String message = "Failure:  added duplicate test rather than maintaining uniqueness of tests";
        Assert.assertEquals(actual, expected, message);
    }

    @Test
    public void testToString() {
        ExecutedTest test = ExecutedTest.getInstance("test", Scenario.getInstance("scenario 1"));
        String expected =
                "{\n  \"subject\": \"subject\",\n" +
                        "  \"tests\": [\n    {\n" +
                        "      \"test\": \"test\",\n" +
                        "      \"scenarios\": [\n        {\n          \"scenario\": \"scenario 1\"\n" +
                        "        }\n      ]\n    }\n  ]\n}";
        TestedSubject subject = TestedSubject.getInstance("subject", test);
        String actual = subject.toString();
        String message = "Failed to return expected JSON formatted TestedSubject";
        Assert.assertEquals(actual, expected, message);
    }

    @Test
    public void testEquals_null() {
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance("scenario"), Scenario.getInstance(1));
        ExecutedTest test = ExecutedTest.getInstance("test", scenarios);
        TestedSubject subject = TestedSubject.getInstance("subject", test);
        //noinspection SimplifiedTestNGAssertion,ConstantConditions
        Assert.assertFalse(subject.equals(null), "Failed to return 'false' when comparator is null");
    }

    @Test
    public void testEquals_self() {
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance("scenario"), Scenario.getInstance(1));
        ExecutedTest test = ExecutedTest.getInstance("test", scenarios);
        TestedSubject subject = TestedSubject.getInstance("subject", test);
        //noinspection EqualsWithItself,SimplifiedTestNGAssertion
        Assert.assertTrue(subject.equals(subject), "Failed to return 'true' when comparator is self");
    }

    @Test
    public void testEquals_differentType() {
        String comparator = "Different Type";
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance("scenario"), Scenario.getInstance(1));
        ExecutedTest test = ExecutedTest.getInstance("test", scenarios);
        TestedSubject subject = TestedSubject.getInstance("subject", test);
        String message = "Failed to return 'false' when ExecutedTest compared to different Type";
        //noinspection SimplifiedTestNGAssertion,EqualsBetweenInconvertibleTypes
        Assert.assertFalse(subject.equals(comparator), message);
    }

    @Test
    public void testEquals_duplicateObjects() {
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance("scenario"), Scenario.getInstance(1));
        ExecutedTest test = ExecutedTest.getInstance("test", scenarios);
        TestedSubject subject_1 = TestedSubject.getInstance("subject", test);
        TestedSubject subject_2 = TestedSubject.getInstance("subject", test);
        String message = "Failed to return 'true' when comparator is identical to ExecutedTest";
        //noinspection SimplifiedTestNGAssertion
        Assert.assertTrue(subject_1.equals(subject_2), message);
    }

    @Test
    public void testHashCode_same() {
        Scenario expectedScenario = Scenario.getInstance("scenario");
        ExecutedTest expectedTest = ExecutedTest.getInstance("test", expectedScenario);
        int expected = TestedSubject.getInstance("subject", expectedTest).hashCode();
        Scenario actualScenario = Scenario.getInstance("scenario");
        ExecutedTest actualTest = ExecutedTest.getInstance("test", actualScenario);
        int actual = TestedSubject.getInstance("subject", actualTest).hashCode();
        Assert.assertEquals(actual, expected, "Failed to return same int for two instances with same values");
    }

    @Test
    public void testHashCode_different() {
        Scenario expectedScenario = Scenario.getInstance("scenario");
        ExecutedTest expectedTest = ExecutedTest.getInstance("test", expectedScenario);
        int expected = TestedSubject.getInstance("subject_1", expectedTest).hashCode();
        Scenario actualScenario = Scenario.getInstance("scenario");
        ExecutedTest actualTest = ExecutedTest.getInstance("test", actualScenario);
        int actual = TestedSubject.getInstance("subject_2", actualTest).hashCode();
        String messageFormat = "Failed to return different int for two instances with different values: expected %d, actual %d";
        Assert.assertNotEquals(actual, expected, String.format(messageFormat, expected, actual));
    }

    @Test
    public void testGetTestCount() {
        int expected = 3;
        ExecutedTest test_1 = ExecutedTest.getInstance("test 1");
        ExecutedTest test_2 = ExecutedTest.getInstance("test 2");
        ExecutedTest test_3 = ExecutedTest.getInstance("test 3");
        List<ExecutedTest> tests = Arrays.asList(test_1, test_2, test_3);
        TestedSubject subject = TestedSubject.getInstance("subject", tests);
        int actual = subject.getTestCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of tests");
    }

    @Test
    public void testGetScenarioCount() {
        int expected = 3;
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance(999), Scenario.getInstance(false));
        ExecutedTest test_1 = ExecutedTest.getInstance("test 1", Scenario.getInstance("scenario"));
        ExecutedTest test_2 = ExecutedTest.getInstance("test 2", scenarios);
        ExecutedTest test_3 = ExecutedTest.getInstance("test 3");
        List<ExecutedTest> tests = Arrays.asList(test_1, test_2, test_3);
        TestedSubject subject = TestedSubject.getInstance("subject", tests);
        int actual = subject.getScenarioCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of tests");
    }
}
