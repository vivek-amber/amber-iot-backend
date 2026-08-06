package com.device.dto;

import com.device.enums.FanSpeed;

import lombok.Data;

@Data
public class ControlDefaults {

    private Integer temperature;

    private FanSpeed fanSpeed;

    private Boolean sleep;

    private Boolean turbo;

    private Boolean eco;

}
