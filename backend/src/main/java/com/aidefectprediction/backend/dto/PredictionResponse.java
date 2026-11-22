package com.aidefectprediction.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PredictionResponse {

    @JsonProperty("predicted_defect")
    private boolean predictedDefect;

    @JsonProperty("probability_of_defect")
    private double probabilityOfDefect;

    @JsonProperty("model")
    private String model;

    public PredictionResponse() {
    }

    public PredictionResponse(
            boolean predictedDefect,
            double probabilityOfDefect,
            String model) {

        this.predictedDefect = predictedDefect;
        this.probabilityOfDefect = probabilityOfDefect;
        this.model = model;
    }

    public boolean isPredictedDefect() {
        return predictedDefect;
    }

    public void setPredictedDefect(boolean predictedDefect) {
        this.predictedDefect = predictedDefect;
    }

    public double getProbabilityOfDefect() {
        return probabilityOfDefect;
    }

    public void setProbabilityOfDefect(double probabilityOfDefect) {
        this.probabilityOfDefect = probabilityOfDefect;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}