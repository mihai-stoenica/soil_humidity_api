package com.soil_humidity_api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Pattern {

    STEP("step"),
    CONTINUOUS("continuous");

    private final String value;

    Pattern(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Pattern fromValue(String value) {
        for (Pattern pattern : Pattern.values()) {
            if (pattern.value.equalsIgnoreCase(value)) {
                return pattern;
            }
        }
        throw new IllegalArgumentException("Unknown pattern: " + value);
    }
}
