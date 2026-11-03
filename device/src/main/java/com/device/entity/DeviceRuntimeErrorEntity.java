package com.device.entity;

import java.time.Instant;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "device_runtime_error")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter 
public class DeviceRuntimeErrorEntity {

    @Id 
    private String deviceId;

    private String companyId;

    @JdbcTypeCode (SqlTypes.JSON)
    @Column (columnDefinition = "jsonb")
    private List<Integer> activeErrors;

    @UpdateTimestamp  
    private Instant updatedAt;

}