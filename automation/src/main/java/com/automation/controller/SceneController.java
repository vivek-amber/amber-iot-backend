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

import com.automation.dto.CreateSceneRequest;
import com.automation.dto.SceneResponse;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.automation.service.SceneService;
import com.automation.utils.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/automation/scene")
@RequiredArgsConstructor
public class SceneController {

    private final SceneService sceneService;

    @PostMapping
    public ResponseEntity<ApiResponse<Map<Object, Object>>> createScene(
            @Valid @RequestBody CreateSceneRequest request) {

        return ResponseEntity.ok(sceneService.createScene(request));

    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SceneResponse>>> getAllScenes() {

        return ResponseEntity.ok(sceneService.getAllScenes());

    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> updateScene(
            @PathVariable String id,
            @Valid @RequestBody CreateSceneRequest request) {

        return ResponseEntity.ok(sceneService.updateScene(id, request));
    }

    @PostMapping("/{id}/execute")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> executeScene(
            @PathVariable String id) {

        return ResponseEntity.ok(sceneService.executeScene(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Map<Object, Object>>> deleteScene(
            @PathVariable String id) {

        return ResponseEntity.ok(sceneService.deleteScene(id));
    }

}
