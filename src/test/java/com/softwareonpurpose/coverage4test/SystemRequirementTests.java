package com.softwareonpurpose.coverage4test;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Test
public class SystemRequirementTests {
    @Test
    public void testGetInstance_subjectList() {
        String message = "Failed to return instantiated SystemRequirement";
        Collection<TestedSubject> testedSubjects = new ArrayList<>();
        SystemRequirement actual = SystemRequirement.getInstance("requirement id", testedSubjects);
        Assert.assertEquals(actual.getClass(), SystemRequirement.class, message);
    }

    @Test
    public void testGetInstance_subject() {
        String message = "Failed to return instantiated SystemRequirement";
        SystemRequirement actual = SystemRequirement.getInstance("requirement id", TestedSubject.getInstance("subject"));
        Assert.assertEquals(actual.getClass(), SystemRequirement.class, message);
    }

    @Test
    public void testHashCode_same() {
        Scenario expectedScenario = Scenario.getInstance("scenario");
        ExecutedTest expectedTest = ExecutedTest.getInstance("test", expectedScenario);
        TestedSubject expectedSubject = TestedSubject.getInstance("subject", expectedTest);
        Scenario actualScenario = Scenario.getInstance("scenario");
        ExecutedTest actualTest = ExecutedTest.getInstance("test", actualScenario);
        TestedSubject actualSubject = TestedSubject.getInstance("subject", actualTest);
        int expected = SystemRequirement.getInstance("requirement_id", expectedSubject).hashCode();
        int actual = SystemRequirement.getInstance("requirement_id", actualSubject).hashCode();
        Assert.assertEquals(actual, expected, "Failed to return same int for two instances with same values");
    }

    @Test
    public void testHashCode_different() {
        Scenario expectedScenario = Scenario.getInstance("scenario");
        ExecutedTest expectedTest = ExecutedTest.getInstance("test", expectedScenario);
        TestedSubject expectedSubject = TestedSubject.getInstance("subject", expectedTest);
        Scenario actualScenario = Scenario.getInstance("scenario");
        ExecutedTest actualTest = ExecutedTest.getInstance("test", actualScenario);
        TestedSubject actualSubject = TestedSubject.getInstance("subject", actualTest);
        int expected = SystemRequirement.getInstance("requirement_1", expectedSubject).hashCode();
        int actual = SystemRequirement.getInstance("requirement_2", actualSubject).hashCode();
        String messageFormat = "Failed to return different int for two instances with different values: expected %d, actual %d";
        Assert.assertNotEquals(actual, expected, String.format(messageFormat, expected, actual));
    }

    @Test
    public void testAddTestedSubject_null() {
        TestedSubject testedSubject = TestedSubject.getInstance("subject description");
        SystemRequirement requirement = SystemRequirement.getInstance("requirement id", testedSubject);
        requirement.addTestedSubject(null);
        int expected = 1;
        int actual = requirement.getTestedSubjects().size();
        Assert.assertEquals(actual, expected, "Failed:  Added <null> to list of TestedSubjects");
    }

    @Test
    public void testAddTestedSubject_sameSubject() {
        TestedSubject subject = TestedSubject.getInstance("subject");
        SystemRequirement requirement = SystemRequirement.getInstance("requirement id", subject);
        requirement.addTestedSubject(subject);
        int expected = 1;
        int actual = requirement.getTestedSubjects().size();
        Assert.assertEquals(actual, expected, "Failed:  Added <null> to list of TestedSubjects");
    }

    @Test
    public void testAddTestedSubject_duplicateSubject() {
        TestedSubject subject = TestedSubject.getInstance("subject 1");
        TestedSubject duplicateSubject = TestedSubject.getInstance("subject 1");
        SystemRequirement requirement = SystemRequirement.getInstance("requirement id", subject);
        requirement.addTestedSubject(duplicateSubject);
        int expected = 1;
        int actual = requirement.getTestedSubjects().size();
        Assert.assertEquals(actual, expected, "Failed:  Added <null> to list of TestedSubjects");
    }

    @Test
    public void testAddTestedSubject_differentSubjectDescription() {
        TestedSubject subject_1 = TestedSubject.getInstance("subject 1");
        TestedSubject subject_2 = TestedSubject.getInstance("subject 2");
        SystemRequirement requirement = SystemRequirement.getInstance("requirement id", subject_1);
        requirement.addTestedSubject(subject_2);
        int expected = 2;
        int actual = requirement.getTestedSubjects().size();
        Assert.assertEquals(actual, expected, "Failed:  Added <null> to list of TestedSubjects");
    }

    @Test
    public void testAddTestedSubject_sameSubjectDifferentTests() {
        TestedSubject subject_1 = TestedSubject.getInstance("subject 1");
        TestedSubject subject_2 = TestedSubject.getInstance("subject 1");
        SystemRequirement requirement = SystemRequirement.getInstance("requirement id", subject_1);
        requirement.addTestedSubject(subject_2);
        int expected = 1;
        int actual = requirement.getTestedSubjects().size();
        Assert.assertEquals(actual, expected, "Failed:  Added <null> to list of TestedSubjects");
    }

    @Test
    public void testAddTestedSubject_sameSubjectAndTestDifferentScenarios() {
        Scenario scenario_1 = Scenario.getInstance("scenario 1");
        Scenario scenario_2 = Scenario.getInstance(999);
        ExecutedTest test_1a = ExecutedTest.getInstance("test 1", scenario_1);
        ExecutedTest test_1b = ExecutedTest.getInstance("test 1", scenario_2);
        TestedSubject subject_1 = TestedSubject.getInstance("subject 1", test_1a);
        TestedSubject subject_2 = TestedSubject.getInstance("subject 1", test_1b);
        SystemRequirement requirement = SystemRequirement.getInstance("requirement id", subject_1);
        requirement.addTestedSubject(subject_2);
        int expected = 1;
        int actual = requirement.getTestedSubjects().size();
        Assert.assertEquals(actual, expected, "Failed:  Added <null> to list of TestedSubjects");
    }

    @Test
    public void testAddTestedSubjects_null() {
        TestedSubject testedSubject = TestedSubject.getInstance("subject description");
        SystemRequirement requirement = SystemRequirement.getInstance("requirement id", testedSubject);
        requirement.addTestedSubjects(null);
        int expected = 1;
        int actual = requirement.getTestedSubjects().size();
        Assert.assertEquals(actual, expected, "Failed:  Added <null> to list of TestedSubjects");
    }

    @Test
    public void testAddTestedSubjects_empty() {
        TestedSubject testedSubject = TestedSubject.getInstance("subject description");
        SystemRequirement requirement = SystemRequirement.getInstance("requirement id", testedSubject);
        requirement.addTestedSubjects(Collections.emptyList());
        int expected = 1;
        int actual = requirement.getTestedSubjects().size();
        Assert.assertEquals(actual, expected, "Failed:  Added subject(s) when provided empty list");
    }

    @Test
    public void testAddTestedSubjects_populated() {
        TestedSubject testedSubject = TestedSubject.getInstance("subject description");
        TestedSubject additionalSubject = TestedSubject.getInstance("additional description");
        SystemRequirement requirement = SystemRequirement.getInstance("requirement id", testedSubject);
        requirement.addTestedSubjects(Collections.singletonList(additionalSubject));
        int expected = 2;
        int actual = requirement.getTestedSubjects().size();
        Assert.assertEquals(actual, expected, "Failed:  Added subject(s) when provided empty list");
    }

    @Test
    public void testToString() {
        String SCENARIO_1_OBJECT = "Scenario 1";
        int SCENARIO_2_OBJECT = 999;
        String TEST_1_DESCRIPTION = "test 1";
        String TEST_2_DESCRIPTION = "test 2";
        String TEST_3_DESCRIPTION = "test 3";
        String SUBJECT_1_DESCRIPTION = "subject 1";
        final String SUBJECT_2_DESCRIPTION = "subject 2";
        String REQUIREMENT_ID = "requirement id";
        Scenario scenario_1 = Scenario.getInstance(SCENARIO_1_OBJECT);
        Scenario scenario_2 = Scenario.getInstance(SCENARIO_2_OBJECT);
        Collection<Scenario> scenarios = Arrays.asList(scenario_1, scenario_2);
        ExecutedTest test_1 = ExecutedTest.getInstance(TEST_1_DESCRIPTION);
        ExecutedTest test_2 = ExecutedTest.getInstance(TEST_2_DESCRIPTION, scenario_1);
        ExecutedTest test_3 = ExecutedTest.getInstance(TEST_3_DESCRIPTION, scenarios);
        Collection<ExecutedTest> tests = Arrays.asList(test_1, test_2, test_3);
        TestedSubject subject_1 = TestedSubject.getInstance(SUBJECT_1_DESCRIPTION, tests);
        TestedSubject subject_2 = TestedSubject.getInstance(SUBJECT_2_DESCRIPTION, test_1);
        SystemRequirement requirement = SystemRequirement.getInstance(REQUIREMENT_ID, subject_1);
        requirement.addTestedSubject(subject_2);
        String expected = String.format("{\n" +
                        "  \"id\": \"%s\",\n" +
                        "  \"subjects\": [\n" +
                        "    {\n" +
                        "      \"subject\": \"%s\",\n" +
                        "      \"tests\": [\n" +
                        "        {\n" +
                        "          \"test\": \"%s\"\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"test\": \"%s\",\n" +
                        "          \"scenarios\": [\n" +
                        "            {\n" +
                        "              \"scenario\": \"%s\"\n" +
                        "            }\n" +
                        "          ]\n" +
                        "        },\n" +
                        "        {\n" +
                        "          \"test\": \"%s\",\n" +
                        "          \"scenarios\": [\n" +
                        "            {\n" +
                        "              \"scenario\": \"%s\"\n" +
                        "            },\n" +
                        "            {\n" +
                        "              \"scenario\": %d\n" +
                        "            }\n" +
                        "          ]\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"subject\": \"%s\",\n" +
                        "      \"tests\": [\n" +
                        "        {\n" +
                        "          \"test\": \"%s\"\n" +
                        "        }\n" +
                        "      ]\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}",
                REQUIREMENT_ID, SUBJECT_1_DESCRIPTION, TEST_1_DESCRIPTION, TEST_2_DESCRIPTION, SCENARIO_1_OBJECT,
                TEST_3_DESCRIPTION, SCENARIO_1_OBJECT, SCENARIO_2_OBJECT, SUBJECT_2_DESCRIPTION, TEST_1_DESCRIPTION);
        String actual = requirement.toString();
        Assert.assertEquals(actual, expected, "Failed to return expected JSON formatted String");
    }

    @Test
    public void testGetSubjectCount() {
        TestedSubject subject_1 = TestedSubject.getInstance("subject 1");
        TestedSubject subject_2 = TestedSubject.getInstance("subject 2");
        TestedSubject subject_3 = TestedSubject.getInstance("subject 3");
        Collection<TestedSubject> subjects = Arrays.asList(subject_1, subject_2, subject_3);
        SystemRequirement requirement = SystemRequirement.getInstance("requirement", subjects);
        int expected = 3;
        int actual = requirement.getSubjectCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of tested subjects");
    }

    @Test
    public void testGetSubjectCount_emptyList() {
        SystemRequirement requirement = SystemRequirement.getInstance("requirement", (TestedSubject) null);
        int expected = 0;
        int actual = requirement.getSubjectCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of tested subjects");
    }

    @Test
    public void testGetTestExecutionCount() {
        ExecutedTest test_1 = ExecutedTest.getInstance("test 1");
        ExecutedTest test_2 = ExecutedTest.getInstance("test 2");
        ExecutedTest test_3 = ExecutedTest.getInstance("test 3");
        Collection<ExecutedTest> tests = Arrays.asList(test_2, test_3);
        TestedSubject subject_1 = TestedSubject.getInstance("subject 1");
        TestedSubject subject_2 = TestedSubject.getInstance("subject 2", test_1);
        TestedSubject subject_3 = TestedSubject.getInstance("subject 2", tests);
        Collection<TestedSubject> subjects = Arrays.asList(subject_1, subject_2, subject_3);
        SystemRequirement requirement = SystemRequirement.getInstance("requirement description", subjects);
        int expected = 3;
        int actual = requirement.getTestExecutionCount();
        Assert.assertEquals(actual, expected, "Failed to get accurate count of executed tests");
    }

    @Test
    public void testEquals_duplicate() {
        TestedSubject subject_1 = TestedSubject.getInstance("subject 1");
        TestedSubject subject_2 = TestedSubject.getInstance("subject 2");
        SystemRequirement requirement_1 = SystemRequirement.getInstance("requirement", subject_1);
        SystemRequirement requirement_2 = SystemRequirement.getInstance("requirement", subject_2);
        String message = "Failed to return true when comparator has same description";
        //noinspection SimplifiedTestNGAssertion
        Assert.assertTrue(requirement_2.equals(requirement_1), message);
    }

    @Test
    public void testEquals_self() {
        TestedSubject subject = TestedSubject.getInstance("subject");
        SystemRequirement requirement = SystemRequirement.getInstance("requirement", subject);
        String message = "Failed to return true when comparator is self";
        //noinspection SimplifiedTestNGAssertion,EqualsWithItself
        Assert.assertTrue(requirement.equals(requirement), message);
    }

    @Test
    public void testEquals_differentDescription() {
        TestedSubject subject = TestedSubject.getInstance("subject");
        SystemRequirement requirement_1 = SystemRequirement.getInstance("requirement 1", subject);
        SystemRequirement requirement_2 = SystemRequirement.getInstance("requirement 2", subject);
        String message = "Failed to return false when comparator has a different description";
        //noinspection SimplifiedTestNGAssertion
        Assert.assertFalse(requirement_2.equals(requirement_1), message);
    }

    @Test
    public void testEquals_differentType() {
        TestedSubject subject = TestedSubject.getInstance("subject");
        String requirement_1 = "requirement 1";
        SystemRequirement requirement_2 = SystemRequirement.getInstance("requirement 2", subject);
        String message = "Failed to return false when comparator is a different Type";
        //noinspection SimplifiedTestNGAssertion,EqualsBetweenInconvertibleTypes
        Assert.assertFalse(requirement_2.equals(requirement_1), message);
    }

    @Test
    public void testEquals_null() {
        TestedSubject subject = TestedSubject.getInstance("subject");
        SystemRequirement requirement = SystemRequirement.getInstance("requirement", subject);
        String message = "Failed to return false when comparator is null";
        //noinspection SimplifiedTestNGAssertion,ConstantConditions
        Assert.assertFalse(requirement.equals(null), message);
    }

    @Test
    public void testCompareTo_null() {
        TestedSubject subject = TestedSubject.getInstance("subject");
        SystemRequirement requirement = SystemRequirement.getInstance("requirement", subject);
        String message = "Failed to return non-zero int when comparator is null";
        Assert.assertTrue(requirement.compareTo(null) < 0, message);
    }

    @Test
    public void testCompareTo_bothDescriptionsNull() {
        TestedSubject subject_1 = TestedSubject.getInstance("subject 1");
        TestedSubject subject_2 = TestedSubject.getInstance("subject 2");
        SystemRequirement requirement = SystemRequirement.getInstance(null, subject_1);
        SystemRequirement comparator = SystemRequirement.getInstance(null, subject_2);
        String message = "Failed to return zero int when both descriptions are null";
        //noinspection SimplifiedTestNGAssertion
        Assert.assertTrue(requirement.compareTo(comparator) == 0, message);
    }

    @Test
    public void testCompareTo_comparatorDescriptionNull() {
        TestedSubject subject = TestedSubject.getInstance("subject");
        SystemRequirement requirement = SystemRequirement.getInstance("requirement", subject);
        SystemRequirement comparator = SystemRequirement.getInstance(null, subject);
        String message = "Failed to return non-zero int when comparator description is null";
        //noinspection SimplifiedTestNGAssertion
        Assert.assertFalse(requirement.compareTo(comparator) == 0, message);
    }

    @Test
    public void testCompareTo_selfDescriptionNull() {
        TestedSubject subject = TestedSubject.getInstance("subject");
        SystemRequirement requirement = SystemRequirement.getInstance(null, subject);
        SystemRequirement comparator = SystemRequirement.getInstance("requirement", subject);
        String message = "Failed to return non-zero int when comparator description is null";
        //noinspection SimplifiedTestNGAssertion
        Assert.assertFalse(requirement.compareTo(comparator) == 0, message);
    }

    @Test
    public void testCompareTo_self() {
        TestedSubject subject = TestedSubject.getInstance("subject");
        SystemRequirement requirement = SystemRequirement.getInstance("requirement", subject);
        String message = "Failed to return zero int when comparator is self";
        //noinspection EqualsWithItself,SimplifiedTestNGAssertion
        Assert.assertTrue(requirement.compareTo(requirement) == 0, message);
    }
}
