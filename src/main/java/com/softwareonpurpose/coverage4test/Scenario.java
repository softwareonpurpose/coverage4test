package com.softwareonpurpose.coverage4test;

import com.google.gson.Gson;

public class Scenario implements Comparable {
    private final Object scenario;

    private Scenario(Object scenario) {
        this.scenario = scenario;
    }

    public static Scenario getInstance(Object scenario) {
        return new Scenario(scenario);
    }

    @Override
    public int compareTo(Object comparator) {
        Gson gson = new Gson();
        Object comparatorScenario = ((Scenario) comparator).scenario;
        return gson.toJson(this.scenario).compareTo(gson.toJson(comparatorScenario));
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    Object getDetail() {
        return scenario;
    }
}
