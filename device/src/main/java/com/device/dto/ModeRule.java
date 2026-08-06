package com.device.dto;

import java.util.List;
import java.util.Map;

import com.device.enums.Control;
import com.device.enums.Mode;

import lombok.Data;

@Data
public class ModeRule {

    private Mode mode;

    private List<Control> disabledControls;

    private ControlDefaults defaults;

    private Map<Control, String> messages;

}