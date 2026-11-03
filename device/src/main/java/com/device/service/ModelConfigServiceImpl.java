package com.device.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.device.repository.DeviceErrorRepository;
import com.device.repository.ModelConfigRepository;
import com.device.repository.ModelRepository;

import com.device.dto.SaveModelConfigRequest;
import com.device.entity.DeviceErrorEntity;
import com.device.entity.ModelConfigEntity;
import com.device.entity.ModelEntity;
import com.device.exception.ModelConfigNotFoundException;
import com.device.exception.ModelNotFoundException;
import com.device.helper.ApiResponse;


@Service
@RequiredArgsConstructor
public class ModelConfigServiceImpl implements ModelConfigService {

    private final ModelConfigRepository repository;
    private final ModelRepository modelRepository;
    private final DeviceErrorRepository deviceErrorRepository;

    @Override
    public ApiResponse<ModelConfigEntity> saveConfiguration(SaveModelConfigRequest request) {

        ModelConfigEntity config = new ModelConfigEntity();

        config.setModelId(request.getModelId());
        config.setConfiguration(request.getConfiguration());

        if (request.getModelId() != null) {
            
            ModelEntity model = modelRepository.findById(request.getModelId()).orElseThrow(
                ()-> new ModelNotFoundException("No model with id found.")
            );
            DeviceErrorEntity catalog = deviceErrorRepository.getReferenceById(request.getErrorId());

            model.setErrorCatalog(catalog);
            modelRepository.save(model);
        }

        repository.save(config);

        return ApiResponse.success("Configuration saved successfully", null);
    }

    @Override
    public ApiResponse<ModelConfigEntity> getConfiguration(String modelNumber) {

        ModelConfigEntity config = repository.findById(modelNumber)
                .orElseThrow(() ->new ModelConfigNotFoundException("Configuration not found"));

        return ApiResponse.success("Configuration fetched successfully",config);
    }
}