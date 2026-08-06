package com.device.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Mode {

    COOL("cool"),
    HEAT("heat"),
    DRY("dry"),
    FAN("fan"),
    AUTO("auto");

    private final String value;

    Mode(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}