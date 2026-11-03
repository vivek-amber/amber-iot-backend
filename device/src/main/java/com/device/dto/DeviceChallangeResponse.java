/*
 * @@file: 
 * @@brief: 
 * @@author: Vivek
 * @@date: 
 */
package com.device.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder  
public class DeviceChallangeResponse {

    private String nonce;
}
