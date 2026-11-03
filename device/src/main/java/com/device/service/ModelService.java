package com.device.service;

import java.util.Map;

import com.device.dto.CreateModelRequest;
import com.device.dto.UpdateModelRequest;
import com.device.entity.ModelEntity;
import com.device.helper.ApiResponse;

public interface ModelService {

    ApiResponse<ModelEntity> createModel(CreateModelRequest model);

    ApiResponse<ModelEntity> getModel(String modelId);

    ApiResponse<Map<Object, Object>> updateModel(String modelId, UpdateModelRequest request);
}