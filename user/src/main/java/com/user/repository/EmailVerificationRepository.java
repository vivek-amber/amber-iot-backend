package com.user.repository;


import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.entity.VerificationOtpEntity;

import jakarta.transaction.Transactional;

public interface EmailVerificationRepository
        extends JpaRepository<VerificationOtpEntity, String> {

        Optional<VerificationOtpEntity> findTopByEmailAndVerifiedFalseOrderByCreatedAtDesc(String email);
        
        @Transactional
        void deleteByEmail(String email);

        void deleteByExpiresAtBefore(Instant instant);
}