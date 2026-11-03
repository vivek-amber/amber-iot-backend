package com.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.device.entity.DeviceErrorEntity;

public interface DeviceErrorRepository extends JpaRepository<DeviceErrorEntity,String>{

}
