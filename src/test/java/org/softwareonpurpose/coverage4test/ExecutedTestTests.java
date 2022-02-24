/*Copyright 2018 - 2022 Craig A. Stockton

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.*/
package org.softwareonpurpose.coverage4test;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Test
public class ExecutedTestTests {
    @DataProvider
    public static Object[][] getInstanceScenarios() {
        return new Object[][]{
                {null}
                , {""}
        };
    }

    @DataProvider
    public static Object[][] getScenarioCountScenarios() {
        return new Object[][]{
                {Scenario.getInstance("scenario 1"), 1},
                {null, 0}
        };
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testGetInstance_withScenario() {
        Class expected = ExecutedTest.class;
        //noinspection ConstantConditions
        Class actual = ExecutedTest.getInstance("test 1", "feature 1", 1, Scenario.getInstance("scenario 1")).getClass();
        String failureFormat = "Failed to return an instance of %s when Scenario provided";
        Assert.assertEquals(actual, expected, String.format(failureFormat, expected));
    }

    @Test(dataProvider = "getInstanceScenarios")
    public void testGetInstance_withNullOrEmptyTestName(String testName) {
        ExecutedTest expected = null;
        ExecutedTest actual = ExecutedTest.getInstance(testName, "feature", 1, Scenario.getInstance(2));
        Assert.assertEquals(actual, expected, String.format("TestName set to '%s' failed to return 'NULL'", testName));
    }

    @Test(dataProvider = "getInstanceScenarios")
    public void testGetInstance_withNullOrEmptyTestSubject(String testSubject) {
        ExecutedTest expected = null;
        ExecutedTest actual = ExecutedTest.getInstance("test 1", testSubject, 1, Scenario.getInstance(1));
        Assert.assertEquals(actual, expected, String.format("TestSubject set to '%s' failed to return 'NULL'", testSubject));
    }

    @Test
    public void testEquals_self() {
        List<Scenario> scenarios = Arrays.asList(Scenario.getInstance("scenario 1"), Scenario.getInstance(1));
        ExecutedTest test = ExecutedTest.getInstance("test 1", "feature 1", 1, Scenario.getInstance("scenario 1"));
        //noinspection EqualsWithItself,SimplifiedTestNGAssertion,ConstantConditions
        Assert.assertTrue(test.equals(test), "Failed to return 'true' when ExecutedTest compared to self");
    }

    @Test
    public void testEquals_differentType() {
        String comparator = "Different Type";
        ExecutedTest test = ExecutedTest.getInstance("test 1", "feature 1", 1, Scenario.getInstance("scenario 1"));
        String message = "Failed to return 'false' when ExecutedTest compared to different Type";
        //noinspection SimplifiableAssertion,ConstantConditions,EqualsBetweenInconvertibleTypes
        Assert.assertFalse(test.equals(comparator), message);
    }

    @Test
    public void testEquals_duplicateObjects() {
        ExecutedTest test_1 = ExecutedTest.getInstance("test 1", "feature 1", 1, Scenario.getInstance("scenario 1"));
        ExecutedTest test_2 = ExecutedTest.getInstance("test 1", "feature 1", 1, Scenario.getInstance("scenario 1"));
        String message = "Failed to return 'true' when comparator is identical to ExecutedTest";
        //noinspection SimplifiedTestNGAssertion,ConstantConditions
        Assert.assertTrue(test_1.equals(test_2), message);
    }

    @Test
    public void testEquals_nullComparator() {
        ExecutedTest comparator = null;
        ExecutedTest test = ExecutedTest.getInstance("test 1", "feature 1", 1, Scenario.getInstance("scenario 1"));
        //noinspection SimplifiableAssertion,ConstantConditions
        Assert.assertFalse(test.equals(comparator));
    }

    @Test
    public void testCompareTo_self() {
        ExecutedTest test = ExecutedTest.getInstance("test 1", "feature 1", 1, Scenario.getInstance("scenario 1"));
        String message = "Failed to return zero when ExecutedTest compared to self";
        //noinspection EqualsWithItself,ConstantConditions
        Assert.assertEquals(test.compareTo(test), 0, message);
    }

    @Test
    public void testCompareTo_sameTestName() {
        ExecutedTest test_1 = ExecutedTest.getInstance("test", "feature 1", 1, Scenario.getInstance("scenario 1"));
        ExecutedTest test_2 = ExecutedTest.getInstance("test", "feature 1", 1, Scenario.getInstance("scenario 1"));
        String message = "Failed to return zero when comparator has same test description";
        //noinspection ConstantConditions
        Assert.assertEquals(test_2.compareTo(test_1), 0, message);
    }

    @Test
    public void testCompareTo_differentTestName() {
        ExecutedTest test_1 = ExecutedTest.getInstance("test 1", "feature 1", 1, Scenario.getInstance("scenario 1"));
        ExecutedTest test_2 = ExecutedTest.getInstance("test 2", "feature 1", 1, Scenario.getInstance("scenario 1"));
        String message = "Failed to return non-zero when comparator has different test description";
        //noinspection ConstantConditions
        Assert.assertNotEquals(test_2.compareTo(test_1), 0, message);
    }

    @Test (dataProvider = "getScenarioCountScenarios" )
    public void testGetScenarioCount(Scenario scenario, int expected) {
        ExecutedTest test = ExecutedTest.getInstance("test 1", "feature 1", 1, scenario);
        //noinspection ConstantConditions
        int actual = test.getScenarioCount();
        Assert.assertEquals(actual, expected, "Failed to return accurate count of scenarios");
    }

    @Test
    public void testAddScenarios() {
        int expected = 3;
        ExecutedTest test = ExecutedTest.getInstance("test 1", "feature 1", 1, Scenario.getInstance("scenario 1"));
        //noinspection ConstantConditions
        test.addScenarios(new ArrayList<>(Arrays.asList(Scenario.getInstance(99), Scenario.getInstance("member"))));
        int actual = test.getScenarioCount();
        Assert.assertEquals(actual, expected, "Failed to add a scenario list");
    }

    @Test
    public void testAddScenario_Null() {
        int expected = 0;
        ExecutedTest actual = ExecutedTest.getInstance("test 1", "feature 1", 1, null);
        Assert.assertEquals(actual.getScenarios().size(), expected, "Failed to validate a scenario not accepting null");
    }
}
