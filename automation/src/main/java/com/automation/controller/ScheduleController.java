package com.automation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.automation.dto.CreateScheduleRequest;
import com.automation.dto.ScheduleResponse;
import com.automation.service.ScheduleService;
import com.automation.utils.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/automation/scheduler")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<Object, Object>>> createSchedule(
            @Valid @RequestBody CreateScheduleRequest request) {

        return ResponseEntity.ok(scheduleService.createSchedule(request));

    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ScheduleResponse>>> getAllSchedule() {

        return ResponseEntity.ok(scheduleService.getAllSchedules());

    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> updateSchedule(@PathVariable String id, 
            @Valid @RequestBody CreateScheduleRequest request) 
        {

        return ResponseEntity.ok(scheduleService.updateSchedule(id,request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> toggleSchedule(@PathVariable String id) {

        return ResponseEntity.ok(scheduleService.toggleSchedule(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> deleteSchedule(@PathVariable String id) {

        return ResponseEntity.ok(scheduleService.deleteSchedule(id));
    }

}
