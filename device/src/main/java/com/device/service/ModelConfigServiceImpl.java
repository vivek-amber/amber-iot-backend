package com.device.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.device.repository.ModelConfigRepository;
import com.device.dto.SaveModelConfigRequest;
import com.device.entity.ModelConfigEntity;
import com.device.exception.ModelConfigNotFoundException;
import com.device.helper.ApiResponse;


@Service
@RequiredArgsConstructor
public class ModelConfigServiceImpl implements ModelConfigService {

    private final ModelConfigRepository repository;

    @Override
    public ApiResponse<ModelConfigEntity> saveConfiguration(SaveModelConfigRequest request) {

        ModelConfigEntity config = new ModelConfigEntity();

        config.setModelId(request.getModelId());
        config.setConfiguration(request.getConfiguration());

        ModelConfigEntity savedConfig =repository.save(config);

        return ApiResponse.success("Configuration saved successfully",savedConfig);
    }

    @Override
    public ApiResponse<ModelConfigEntity> getConfiguration(String modelNumber) {

        ModelConfigEntity config = repository.findById(modelNumber)
                .orElseThrow(() ->new ModelConfigNotFoundException("Configuration not found"));

        return ApiResponse.success("Configuration fetched successfully",config);
    }
}