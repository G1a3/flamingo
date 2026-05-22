package com.flamingo.qa.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AdditionalNeeds {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner"),
    WIFI("WiFi"),
    PARKING("Parking");

    private final String value;

    AdditionalNeeds(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static AdditionalNeeds fromValue(String value) {
        for (AdditionalNeeds needs : values()) {
            if (needs.value.equalsIgnoreCase(value) || needs.name().equalsIgnoreCase(value)) {
                return needs;
            }
        }
        throw new IllegalArgumentException("Unknown additional need: " + value);
    }
}
