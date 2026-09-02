package com.automation.mapper;

import org.springframework.stereotype.Component;

import com.automation.dto.CreateSceneRequest;
import com.automation.dto.SceneResponse;
import com.automation.entity.SceneEntity;

@Component
public class SceneMapper {

    public SceneEntity toEntity(CreateSceneRequest request, String CompanyId, String userId) {
        SceneEntity entity = new SceneEntity();

        entity.setName(request.getName().trim());
        entity.setEnabled(request.getEnabled());
        entity.setDevices(request.getDevices());
        entity.setCompanyId(CompanyId);
        entity.setUserId(userId);
        
        return entity;
    }

    public SceneResponse toResponse(SceneEntity entity) {
        return new SceneResponse(
                entity.getId(),
                entity.getName(),
                entity.getEnabled(),
                entity.getDevices(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}