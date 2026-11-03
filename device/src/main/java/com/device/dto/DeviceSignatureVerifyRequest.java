/*
 * @@file: 
 * @@brief: 
 * @@author: Vivek
 * @@date: 
 */
package com.device.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeviceSignatureVerifyRequest(

    @NotNull 
    @NotBlank 
    String nonce,

    @NotNull 
    @NotBlank
    String deviceId,

    @NotNull 
    @NotBlank
    String signature

) {

}
