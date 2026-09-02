package com.automation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automation.entity.ScheduleEntity;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, String> {

    List<ScheduleEntity> findAllByUserIdAndCompanyId(String userId, String companyId);
}
