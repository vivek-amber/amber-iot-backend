package com.device.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FanSpeed {

    LOW("low"),
    MIDDLE("middle"),
    HIGH("high"),
    AUTO("auto");

    private final String value;

    FanSpeed(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
