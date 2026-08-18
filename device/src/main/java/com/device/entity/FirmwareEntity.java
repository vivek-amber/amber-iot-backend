package com.device.entity;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "firmware")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FirmwareEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String modelId;

    @Column(nullable = false)
    private String version;

    @Column(nullable = false)
    private String downloadUrl;

    private String releaseNotes;

    @Builder.Default
    private Boolean mandatory = false;

    @Builder.Default
    private Boolean active = true;

    @CreationTimestamp
    private Instant releasedAt;
}