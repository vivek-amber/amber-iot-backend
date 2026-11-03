package com.device.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.device.entity.DeviceEntity;

public interface DeviceRepository extends JpaRepository<DeviceEntity, String> {


    List<DeviceEntity> findByUserId(String userId);

    Optional<DeviceEntity> findByDeviceIdAndUserId(String deviceId, String userId);

}
