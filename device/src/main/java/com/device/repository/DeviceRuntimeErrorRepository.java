package com.device.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.device.entity.DeviceRuntimeErrorEntity;

public interface DeviceRuntimeErrorRepository extends JpaRepository<DeviceRuntimeErrorEntity, String> {

    Optional<DeviceRuntimeErrorEntity> findByDeviceIdAndCompanyId(String deviceId, String companyId);
}
