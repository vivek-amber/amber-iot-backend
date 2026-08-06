package com.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.device.entity.ModelConfigEntity;

public interface ModelConfigRepository extends JpaRepository<ModelConfigEntity, String> {

}