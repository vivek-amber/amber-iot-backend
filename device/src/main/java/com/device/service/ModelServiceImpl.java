package com.device.service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.device.dto.CreateModelRequest;
import com.device.dto.UpdateModelRequest;
import com.device.entity.DeviceErrorEntity;
import com.device.entity.ModelEntity;
import com.device.exception.ModelAlreadyExistsException;
import com.device.exception.ModelNotFoundException;
import com.device.helper.ApiResponse;
import com.device.repository.DeviceErrorRepository;
import com.device.repository.ModelRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;
    private final DeviceErrorRepository deviceErrorRepository;

    @Override
    public ApiResponse<ModelEntity> createModel(CreateModelRequest request) {

        Optional<ModelEntity> exists = modelRepository.findById(request.getId());

        if (exists.isPresent()) {
            throw new ModelAlreadyExistsException("Model with this ID already exists.");
        }
        
        ModelEntity model = new ModelEntity();
        model.setId(request.getId());
        model.setModelName(request.getModelName());
        model.setDeviceType(request.getDeviceType());
        model.setActive(request.getActive());
        
        ModelEntity savedModel = modelRepository.save(model);
        
        return ApiResponse.success("Model created successfully",savedModel);

    }

    @Override
    public ApiResponse<ModelEntity> getModel(String modelId) {

        ModelEntity model = modelRepository.findById(modelId)
                .orElseThrow(() -> new ModelNotFoundException("Model not found"));

        return ApiResponse.success("Model fetched successfully", model);
    }

    @Override
    public ApiResponse<Map<Object,Object>> updateModel(String modelId,UpdateModelRequest request) {

        ModelEntity model = modelRepository.findById(modelId).orElseThrow(() ->
            new ModelNotFoundException("Model not found"));
        if (request.getModelName() != null) {
            model.setModelName(request.getModelName());
        }

        if (request.getDeviceType() != null) {
         model.setDeviceType(request.getDeviceType());
        }

        if (request.getActive() != null) {
            model.setActive(request.getActive());
        }

        if (request.getErrorCatalogId() != null) {
        DeviceErrorEntity errorCatalog =
        deviceErrorRepository.findById(request.getErrorCatalogId())
            .orElseThrow(() ->new RuntimeException("Error catalog not found"));
        model.setErrorCatalog(errorCatalog);
        }
        modelRepository.save(model);

        return ApiResponse.success("Model updated successfully",Collections.emptyMap());
    }
}