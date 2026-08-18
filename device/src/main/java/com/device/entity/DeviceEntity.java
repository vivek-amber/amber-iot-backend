package com.device.entity;

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
@Table(name = "device")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, unique = true)
    private String deviceId;

    @Column(nullable = false)
    private String deviceName;

    @Column(nullable = false)
    private String modelId;

    // @Column(nullable = false)
    private String firmwareVersion="1.0.0";

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant registeredAt;
}