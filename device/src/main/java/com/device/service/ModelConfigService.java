package com.device.service;

import com.device.dto.SaveModelConfigRequest;
import com.device.entity.ModelConfigEntity;
import com.device.helper.ApiResponse;

public interface ModelConfigService {

    ApiResponse<ModelConfigEntity> saveConfiguration(SaveModelConfigRequest request);

    ApiResponse<ModelConfigEntity> getConfiguration(String modelNumber);
}