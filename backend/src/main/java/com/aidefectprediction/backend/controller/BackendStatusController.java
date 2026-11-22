package com.aidefectprediction.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BackendStatusController {

    @GetMapping("/api/status")
    public Map<String, String> checkStatus() {
        return Map.of(
                "status", "UP",
                "message", "AI Defect Prediction Backend is running"
        );
    }
}