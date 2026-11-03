/*
 * @@file: 
 * @@brief: 
 * @@author: Vivek
 * @@date: 
 */
package com.device.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.device.dto.DeviceChallangeResponse;
import com.device.dto.DeviceChallangerequest;
import com.device.dto.DeviceSignatureVerifyRequest;
import com.device.dto.DeviceSignatureVerifyResponse;
import com.device.helper.ApiResponse;
import com.device.service.ProvisionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/device/provision")
@RequiredArgsConstructor 
public class ProvisionController {

    private final ProvisionService provisionService;

    @PostMapping 
    public ResponseEntity<ApiResponse<DeviceChallangeResponse>>getChallangeNonce( @Valid @RequestBody DeviceChallangerequest request){

        return ResponseEntity.ok(provisionService.getChallangeNonce(request));
    }

    @PostMapping("/signature")
    public ResponseEntity<ApiResponse<DeviceSignatureVerifyResponse>>verifySignature( @Valid @RequestBody DeviceSignatureVerifyRequest request){

        return ResponseEntity.ok(provisionService.verifySignature(request));
    }
}
