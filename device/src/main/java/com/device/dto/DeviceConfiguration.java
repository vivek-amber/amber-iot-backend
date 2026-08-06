package com.device.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeviceConfiguration {

    @NotNull(message="Controlls are required")
    private List<ControlDefinition> controls;

    @NotNull(message="Modes are required")
    private List<ModeDefinition> modes;

    private List<ModeRule> modeRules;

    private List<ControlConflictRule> conflictRules;

    private List<QuickActionDefinition> quickActions;

}
