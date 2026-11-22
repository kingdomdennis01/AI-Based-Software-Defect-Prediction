package com.aidefectprediction.backend.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class PredictionRequest {

    @NotEmpty(message = "Features must not be empty")
    private Map<
            @NotBlank(message = "Feature names must not be blank") String,
            @NotNull(message = "Feature values must not be null")
            @DecimalMin(
                    value = "0.0",
                    message = "Feature values must be zero or greater"
            )
                    Double
            > features;

    public PredictionRequest() {
    }

    public Map<String, Double> getFeatures() {
        return features;
    }

    public void setFeatures(Map<String, Double> features) {
        this.features = features;
    }
}