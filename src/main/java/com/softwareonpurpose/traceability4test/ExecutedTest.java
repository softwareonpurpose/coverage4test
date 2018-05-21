package com.softwareonpurpose.traceability4test;

import com.google.gson.Gson;

class ExecutedTest {
    private final String test;

    private ExecutedTest(String testDescription) {
        test = testDescription;
    }

    static ExecutedTest create(String testDescription) {
        return new ExecutedTest(testDescription);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
