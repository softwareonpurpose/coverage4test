package com.softwareonpurpose.traceability4test;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class DataScenarioTest {

    @Test
    public void scenarioToString() {
        String expected = "{\"scenario\":\"scenario description\"}";
        DataScenario scenario = DataScenario.create("scenario description");
        String actual = scenario.toString();
        Assert.assertEquals(actual, expected, "DataScenario.toString() failed to return expected json");
    }
}
