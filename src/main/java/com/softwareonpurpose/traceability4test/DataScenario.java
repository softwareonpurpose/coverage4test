package com.softwareonpurpose.traceability4test;

import com.google.gson.Gson;

class DataScenario {
    private final String scenario;

    private DataScenario(String scenario_description) {
        scenario = scenario_description;
    }

    static DataScenario create(String scenario_description) {
        return new DataScenario(scenario_description);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
