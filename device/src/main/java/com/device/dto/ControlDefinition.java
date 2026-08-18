package com.device.dto;


import java.util.List;

import com.device.enums.ControlType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ControlDefinition {

    @NotBlank(message = "Control code is required.")
    private String code;

    private String displayName;

    @NotBlank
    private String command;

    private List<String> allowedValues;

    @NotNull(message="Control type is required.")
    private ControlType type;

    private Integer minValue;

    private Integer maxValue;

    private Integer step;
}