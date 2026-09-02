package com.automation.mapper;

import org.springframework.stereotype.Component;

import com.automation.dto.CreateScheduleRequest;
import com.automation.dto.ScheduleResponse;
import com.automation.entity.ScheduleEntity;

@Component
public class ScheduleMapper {

    public ScheduleEntity toEntity(CreateScheduleRequest request, String CompanyId, String userId) {
        ScheduleEntity entity = new ScheduleEntity();

        entity.setName(request.getName().trim());
        entity.setEnabled(request.getEnabled());
        entity.setDevice(request.getDevice());
        entity.setScheduleTrigger(request.getScheduleTrigger());
        entity.setCompanyId(CompanyId);
        entity.setUserId(userId);

        return entity;
    }

    public ScheduleResponse toResponse(ScheduleEntity entity) {
        return new ScheduleResponse(
                entity.getId(),
                entity.getName(),
                entity.getEnabled(),
                entity.getDevice(),
                entity.getScheduleTrigger(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }

}