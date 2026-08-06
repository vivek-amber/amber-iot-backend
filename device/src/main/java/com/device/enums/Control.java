package com.device.enums;

public enum Control {

    POWER("power"),
    TEMPERATURE("temperature"),
    MODE("mode"),
    FANSPEED("fanSpeed"),
    SLEEP("sleep"),
    SWING("swing"),
    TURBO("turbo"),
    ECO("economy");

    private final String value;

    Control(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}