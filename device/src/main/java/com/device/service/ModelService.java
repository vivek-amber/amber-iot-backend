package com.device.service;

import com.device.dto.CreateModelRequest;
import com.device.entity.ModelEntity;
import com.device.helper.ApiResponse;

public interface ModelService {

    ApiResponse<ModelEntity> createModel(CreateModelRequest model);

    ApiResponse<ModelEntity> getModel(String modelNumber);

}