package com.softwareonpurpose.traceability4test;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class DataScenarioTest {

    @Test
    public void scenarioToString() {
        String expected = "{\"description\":\"scenario description\"}";
        DataScenario scenario = DataScenario.construct("scenario description");
        String actual = scenario.toString();
        Assert.assertEquals(actual, expected, "DataScenario.toString() failed to return expected json");
    }
}
