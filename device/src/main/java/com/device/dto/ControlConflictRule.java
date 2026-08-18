package com.device.dto;

import java.util.List;

import lombok.Data;

@Data
public class ControlConflictRule {

    private String control;

    private List<String> incompatibleWith;

}