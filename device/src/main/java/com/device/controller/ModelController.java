package com.device.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.device.dto.CreateModelRequest;
import com.device.dto.UpdateModelRequest;
import com.device.entity.ModelEntity;
import com.device.helper.ApiResponse;
import com.device.service.ModelService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/device/model")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    @PostMapping
    public ResponseEntity<ApiResponse<ModelEntity>> createModel(@Valid @RequestBody CreateModelRequest model) {

        return ResponseEntity.status(HttpStatus.CREATED).body(modelService.createModel(model));
    }

    @GetMapping("/{modelId}")
    public ResponseEntity<ApiResponse<ModelEntity>> getModel(@PathVariable String modelId) {

        return ResponseEntity.ok(modelService.getModel(modelId));
    }

    @PatchMapping("/{modelId}")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> updateModel(@PathVariable String modelId,@RequestBody UpdateModelRequest request) {

        return ResponseEntity.ok(modelService.updateModel(modelId, request));
    }
}