package com.softwareonpurpose.traceability4test;

import com.google.gson.Gson;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

class DataScenario implements Comparable<DataScenario> {
    private final String description;

    private DataScenario(String scenario_description) {
        description = scenario_description;
    }

    static DataScenario create(String scenario_description) {
        return new DataScenario(scenario_description);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(description).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof DataScenario)) {
            return false;
        }
        DataScenario comparator = (DataScenario) obj;
        return new EqualsBuilder().append(this.description, comparator.description).isEquals();
    }

    @Override
    public int compareTo(DataScenario comparator) {
        return this.equals(comparator) ? 0 : this.description == null ? -1 : comparator.description == null ? 1
                : this.description.compareTo(comparator.description);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
