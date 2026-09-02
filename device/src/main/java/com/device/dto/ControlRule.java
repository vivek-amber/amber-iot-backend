package com.device.dto;

import java.util.List;
import java.util.Map;


import lombok.Data;

@Data
public class ControlRule {

    private String control;

    private String value;

    private List<String> disabledControls;

    private Map<String, Object> defaults;

    private Map<String, String> messages;

}