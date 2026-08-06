package com.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.device.entity.ModelEntity;

public interface ModelRepository extends JpaRepository<ModelEntity, String> {

}