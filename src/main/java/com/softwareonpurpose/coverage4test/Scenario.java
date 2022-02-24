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

import com.google.gson.Gson;

class Scenario implements Comparable {
    private final Object scenario;

    private Scenario(Object scenario) {
        this.scenario = scenario;
    }

    /***
     * @param scenario
     * @return
     */
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
