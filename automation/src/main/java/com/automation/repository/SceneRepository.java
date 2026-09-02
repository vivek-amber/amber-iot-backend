package com.automation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automation.entity.SceneEntity;
import java.util.Optional;

public interface SceneRepository extends JpaRepository<SceneEntity, String> {

    Boolean existsByUserIdAndNameIgnoreCase(String userId, String name);

    List<SceneEntity> findAllByUserIdAndCompanyId(String userId, String companyId);

    Optional<SceneEntity> findByIdAndUserIdAndCompanyId(
            String id,
            String userId,
            String companyId);

    Boolean existsByUserIdAndCompanyIdAndNameIgnoreCaseAndIdNot(
            String userId,
            String companyId,
            String name,
            String id);
}