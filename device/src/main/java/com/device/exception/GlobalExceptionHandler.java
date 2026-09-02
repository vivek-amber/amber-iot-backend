package com.device.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.device.helper.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<ApiResponse<Map<Object, Object>>> handleUserNotFound(DeviceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(ModelConfigNotFoundException.class)
    public ResponseEntity<ApiResponse<Map<Object, Object>>> handleModelConfigNotFound(ModelConfigNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<ApiResponse<Map<Object, Object>>> handleModelNotFound(ModelNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(ModelAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Map<Object, Object>>> handleModelNotFound(ModelAlreadyExistsException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<Object, Object>>> handleGenericException(Exception ex) {
        
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error("Something went wrong. Please try again later."));
    }
}
