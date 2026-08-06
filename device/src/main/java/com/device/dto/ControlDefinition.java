package com.device.dto;


import com.device.enums.Control;
import com.device.enums.ControlType;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ControlDefinition {

    @NotBlank(message="Control code is required.")
    private Control code;

    private String displayName;

    @NotBlank(message="Control type is required.")
    private ControlType type;

    private Integer minValue;

    private Integer maxValue;

    private Integer step;
}