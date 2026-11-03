package com.device.entity;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import com.device.dto.DeviceErrorDefinition;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "device_error")
@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor 
public class DeviceErrorEntity {

    @Id 
    private String id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private List<DeviceErrorDefinition> errors;

    @OneToMany(
        mappedBy = "errorCatalog",
        fetch = FetchType.LAZY
    )
    private List<ModelEntity> models;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp 
    private Instant updatedAt;
}
