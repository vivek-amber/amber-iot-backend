package com.device.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.device.dto.DeviceErrorDefinition;
import com.device.helper.ApiResponse;
import com.device.service.RuntimeErrorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/device/runtime-error")
@RequiredArgsConstructor 
public class DeviceRuntimeErrorController {

    private final RuntimeErrorService runtimeErrorService;

    @GetMapping("/model/{modelId}")
    public ResponseEntity<ApiResponse<List<DeviceErrorDefinition>>> getCatalog(@PathVariable String modelId) {
        return ResponseEntity.ok(runtimeErrorService.getByModelId(modelId));
    }

    @GetMapping("/{companyId}/{deviceId}")
    public ResponseEntity<ApiResponse<List<Integer>>> getErrors(@PathVariable String companyId,@PathVariable String deviceId) {

        return ResponseEntity.ok(runtimeErrorService.getErrors(companyId,deviceId));
    }
}