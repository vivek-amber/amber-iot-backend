package com.device.entity;

import com.device.enums.DeviceType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "model")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ModelEntity {

        @Id
        @Column(nullable = false, unique = true)
        private String id;

        @Column(nullable = false)
        private String modelName;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private DeviceType deviceType;

        @Column(nullable = false)
        private Boolean active = true;

}