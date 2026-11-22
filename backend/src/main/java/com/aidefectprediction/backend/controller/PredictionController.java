package com.aidefectprediction.backend.controller;

import com.aidefectprediction.backend.dto.PredictionRequest;
import com.aidefectprediction.backend.dto.PredictionResponse;
import com.aidefectprediction.backend.service.PredictionService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/predictions")
public class PredictionController {

    private final PredictionService predictionService;

    public PredictionController(PredictionService predictionService) {
        this.predictionService = predictionService;
    }

    @PostMapping
    public PredictionResponse predict(
            @Valid @RequestBody PredictionRequest request) {

        return predictionService.predict(request);
    }
}
