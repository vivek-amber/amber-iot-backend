package com.device.constants;

public final class MqttTopics {

    private MqttTopics (){}

    public static final String DEVICE_STATUS =
            "company/%s/device/%s/status";

    public static final String DEVICE_COMMAND =
            "company/%s/device/%s/cmd";

    public static final String DEVICE_ACKNOWLEDGEMENT =
            "company/%s/device/%s/ack";

}