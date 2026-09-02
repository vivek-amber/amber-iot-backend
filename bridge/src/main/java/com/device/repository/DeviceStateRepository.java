package com.device.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.device.entity.DeviceStateEntity;


public interface DeviceStateRepository
                extends JpaRepository<DeviceStateEntity, String> {
                
        Optional<DeviceStateEntity> findByCompanyIdAndDeviceId(String companyId, String deviceId);
}
