package com.user.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "email_verification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationOtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable=false)
    private String email;

    @Column(nullable=false)
    private String otp;

    @Column(nullable=false)
    private Boolean verified = false;

    @Column(nullable=false)
    private Integer attempts = 0;

    @Column(nullable=false)
    private Instant expiresAt;

    @CreationTimestamp
    private Instant createdAt;
}