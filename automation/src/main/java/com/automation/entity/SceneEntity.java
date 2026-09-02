package com.automation.entity;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.automation.dto.DeviceAction;

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

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "scenes")
public class SceneEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "user_id", nullable = false, updatable = false, length = 100)
    private String userId;

    @Column(name = "company_id", nullable = false, updatable = false, length = 100)
    private String companyId;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = Boolean.TRUE;


    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "devices", nullable = false, columnDefinition = "jsonb")
    private List<DeviceAction> devices;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

}