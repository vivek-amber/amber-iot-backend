package com.user.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "user_sessions",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {
                "user_id",
                "device_id"
            }
        )
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSessionEntity {

    @Id
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "refresh_token_hash", nullable = false)
    private String refreshTokenHash;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    @Builder.Default
    private String deviceName = "Unknown";
    
    @Column(name = "device_id", nullable = false)
    private String deviceId;

}