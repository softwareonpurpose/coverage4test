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
        if (comparator == null) {
            return -1;
        }
        if(!comparator.getClass().equals(this.getClass())){
            return -2;
        }
        String comparatorJson;
        Gson gson = new Gson();
        Object comparatorScenario = ((Scenario) comparator).scenario;
        comparatorJson = gson.toJson(comparatorScenario);
        return gson.toJson(this.scenario).compareTo(comparatorJson);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    Object getDetail() {
        return scenario;
    }
}
