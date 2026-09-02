package com.automation.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.automation.utils.ApiResponse;

@Component("userExceptionHandler")
@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(SceneAlreadyExistsException.class)
        public ResponseEntity<ApiResponse<Map<Object, Object>>> handleUserAlreadyExists(
                        SceneAlreadyExistsException ex) {

                return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(ApiResponse.error(ex.getMessage()));
        }

        @ExceptionHandler(UserNotValidException.class)
        public ResponseEntity<ApiResponse<Map<Object, Object>>> handleUserNotFound(
                        UserNotValidException ex) {

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(ApiResponse.error(ex.getMessage()));
        }

        @ExceptionHandler(ScheduleNotFoundException.class)
        public ResponseEntity<ApiResponse<Map<Object, Object>>> handleScheduleNotFound(
                        ScheduleNotFoundException ex) {

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(ApiResponse.error(ex.getMessage()));
        }
        
        @ExceptionHandler(SceneNotFoundException.class)
        public ResponseEntity<ApiResponse<Map<Object, Object>>> handleSceneNotFound(
                        SceneNotFoundException ex) {

                return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body(ApiResponse.error(ex.getMessage()));
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiResponse<Map<Object, Object>>> handleGenericException(
                        Exception ex) {

                ex.printStackTrace();
                return ResponseEntity
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .body(ApiResponse.error(
                                                "Something went wrong. Please try again later."));
        }
}