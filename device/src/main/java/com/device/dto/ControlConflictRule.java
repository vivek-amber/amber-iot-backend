package com.device.dto;

import java.util.List;

import com.device.enums.Control;

import lombok.Data;

@Data
public class ControlConflictRule {

    private Control control;

    private List<Control> incompatibleWith;

}