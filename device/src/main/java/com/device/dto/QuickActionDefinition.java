package com.device.dto;

import com.device.enums.Control;

import lombok.Data;

@Data
public class QuickActionDefinition {

    private Control control;

    private String label;

}