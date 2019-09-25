package com.softwareonpurpose.coverage4test.mock;

import com.google.gson.Gson;

public class ScenarioObject {
    private final String text;
    private final Integer integer;
    private final Boolean aBoolean;

    private ScenarioObject(String textValue, Integer integer, Boolean aBoolean) {
        this.text = textValue;
        this.integer = integer;
        this.aBoolean = aBoolean;
    }

    public static ScenarioObject getInstance(String textValue, Integer integer, Boolean aBoolean) {
        return new ScenarioObject(textValue, integer, aBoolean);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
