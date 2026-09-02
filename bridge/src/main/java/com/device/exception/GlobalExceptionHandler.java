package com.device.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.device.helpers.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MqttPublishException.class)
        public ResponseEntity<ApiResponse<Void>> handleMqttPublishException(
                        MqttPublishException ex) {

                log.error("MQTT publish failed", ex);

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(ApiResponse.error(ex.getMessage()));
        }

        @ExceptionHandler(MqttSubscribeException.class)
        public ResponseEntity<ApiResponse<Void>> handleMqttSubscribeException(
                        MqttSubscribeException ex) {

                log.error("MQTT subscribe failed", ex);

                return ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(ApiResponse.error(ex.getMessage()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<Void>> handleGenericException(
                        Exception ex) {

                log.error("Unhandled exception", ex);

                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.error(
                                                "Something went wrong. Please try again later."));
        }
}