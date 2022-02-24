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
package com.softwareonpurpose.coverage4test;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class ScenarioTests {
    @Test
    public void testGetDetail_null() {
        Long expected = null;
        Scenario scenario = Scenario.getInstance(expected);
        Object actual = scenario.getDetail();
        Assert.assertEquals(actual, expected, "Failed to return null provided as scenario");
    }

    @Test
    public void testGetDetail_instance() {
        long expected = 999L;
        Scenario scenario = Scenario.getInstance(expected);
        Object actual = scenario.getDetail();
        Assert.assertEquals(actual, expected, "Failed to return primitive object provided as scenario");
    }

    @Test
    public void testToString_null() {
        Long testValue = null;
        String expected = "{}";
        Scenario scenario = Scenario.getInstance(testValue);
        String actual = scenario.toString();
        Assert.assertEquals(actual, expected, "Failed to return String representation of null provided as scenario");
    }

    @Test
    public void testToString_instance() {
        long testValue = 999L;
        String expected = String.format("{\"scenario\":%d}", testValue);
        Scenario scenario = Scenario.getInstance(testValue);
        String actual = scenario.toString();
        Assert.assertEquals(actual, expected, "Failed to return String representation of primitive object provided as scenario");
    }

    @Test
    public void testCompareTo_null() {
        Long testValue = null;
        Scenario scenario = Scenario.getInstance(testValue);
        int actual = scenario.compareTo(testValue);
        Assert.assertTrue(actual < 0, "Failed to return negative int for comparison to null");
    }

    @Test
    public void testCompareTo_nonScenarioObject() {
        Long scenarioValue = 1L;
        String testValue = "scenario";
        Scenario scenario = Scenario.getInstance(scenarioValue);
        int actual = scenario.compareTo(testValue);
        Assert.assertTrue(actual < 0, "Failed to return negative int for comparison to null");
    }

    @Test
    public void testCompareTo_differentScenario() {
        Long scenarioValue_1 = 1L;
        String scenarioValue_2 = "scenario";
        Scenario scenario_1 = Scenario.getInstance(scenarioValue_1);
        Scenario scenario_2 = Scenario.getInstance(scenarioValue_2);
        int actual = scenario_1.compareTo(scenario_2);
        String failureFormat = "Failed to return non-zero int for comparison to different Scenario: %d returned";
        Assert.assertNotEquals(actual, 0, String.format(failureFormat, actual));
    }

    @Test
    public void testCompareTo_sameScenario() {
        Long scenarioValue = 1L;
        Scenario scenario = Scenario.getInstance(scenarioValue);
        //noinspection EqualsWithItself     Intention of test is violation of best practice
        int actual = scenario.compareTo(scenario);
        Assert.assertEquals(actual, 0, "Failed to return zero for comparison to same Scenario");
    }

    @Test
    public void testCompareTo_duplicateScenario() {
        Long scenarioValue = 1L;
        Scenario scenario_1 = Scenario.getInstance(scenarioValue);
        Scenario scenario_2 = Scenario.getInstance(scenarioValue);
        int actual = scenario_1.compareTo(scenario_2);
        Assert.assertEquals(actual, 0, "Failed to return zero for comparison to duplicate Scenario");
    }
}
