package com.boom.challenge.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PhotoType {
    REAL_ESTATE("RealEstate"), FOOD("Food"), EVENTS("Events");

    private String value;

    private PhotoType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
