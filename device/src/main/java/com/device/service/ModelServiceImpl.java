package com.device.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.device.dto.CreateModelRequest;
import com.device.entity.ModelEntity;
import com.device.exception.ModelAlreadyExistsException;
import com.device.exception.ModelNotFoundException;
import com.device.helper.ApiResponse;
import com.device.repository.ModelRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;

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
    public ApiResponse<ModelEntity> getModel(String modelNumber) {

        ModelEntity model = modelRepository.findById(modelNumber)
                .orElseThrow(() ->new ModelNotFoundException("Model not found"));

        return ApiResponse.success("Model fetched successfully",model);
    }
}