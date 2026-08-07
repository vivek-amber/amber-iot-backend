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
@Table(name = "power_consumption")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PowerConsumptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String deviceId;

    @Column(nullable = false)
    private Double power;

    @Column(nullable = false)
    private Double energy;

    @CreationTimestamp
    private Instant recordedAt;
}
