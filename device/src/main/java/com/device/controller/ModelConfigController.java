package com.device.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.device.dto.SaveModelConfigRequest;
import com.device.entity.ModelConfigEntity;
import com.device.service.ModelConfigService;

import jakarta.validation.Valid;

import com.device.helper.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/device/model/config")
@RequiredArgsConstructor
public class ModelConfigController {

    private final ModelConfigService service;

    @PostMapping
    public ResponseEntity<ApiResponse<ModelConfigEntity>> saveConfig(@Valid @RequestBody SaveModelConfigRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED).body(service.saveConfiguration(request));
    }

    @GetMapping("/{modelNumber}")
    public ResponseEntity<ApiResponse<ModelConfigEntity>> getConfig(@PathVariable String modelNumber) {

        return ResponseEntity.ok(service.getConfiguration(modelNumber));
    }
}