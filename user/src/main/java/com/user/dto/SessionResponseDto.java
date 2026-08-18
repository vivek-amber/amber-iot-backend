package com.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionResponseDto {

    private String id;
    private Boolean active;
    private String deviceName;
}
