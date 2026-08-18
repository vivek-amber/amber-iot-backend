package com.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.entity.UserSessionEntity;

public interface SessionRepository extends JpaRepository<UserSessionEntity, String> {
    
    List<UserSessionEntity> findByUserId(String userId);

    void deleteAllByUserId(String userId);
}
