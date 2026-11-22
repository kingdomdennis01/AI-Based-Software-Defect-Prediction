package com.aidefectprediction.backend.controller;

import com.aidefectprediction.backend.client.MlServiceClient;
import com.aidefectprediction.backend.dto.PredictionRequest;
import com.aidefectprediction.backend.dto.PredictionResponse;
import com.aidefectprediction.backend.service.PredictionService;

import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestClient;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PredictionControllerTest {

    @Test
    void shouldReturnPredictionThroughController() {

        Map<String, Double> features = new LinkedHashMap<>();

        features.put("loc", 50.0);
        features.put("v(g)", 5.0);
        features.put("ev(g)", 3.0);
        features.put("iv(g)", 4.0);
        features.put("n", 120.0);
        features.put("v", 600.0);
        features.put("l", 0.08);
        features.put("d", 12.0);
        features.put("i", 45.0);
        features.put("e", 7200.0);
        features.put("b", 0.2);
        features.put("t", 400.0);
        features.put("lOCode", 45.0);
        features.put("lOComment", 3.0);
        features.put("lOBlank", 2.0);
        features.put("locCodeAndComment", 1.0);
        features.put("uniq_Op", 15.0);
        features.put("uniq_Opnd", 20.0);
        features.put("total_Op", 70.0);
        features.put("total_Opnd", 50.0);
        features.put("branchCount", 9.0);

        PredictionRequest request = new PredictionRequest();
        request.setFeatures(features);

        MlServiceClient mlServiceClient =
                new MlServiceClient(
                        RestClient.builder(),
                        "http://127.0.0.1:8000"
                );

        PredictionService predictionService =
                new PredictionService(mlServiceClient);

        PredictionController predictionController =
                new PredictionController(predictionService);

        PredictionResponse response =
                predictionController.predict(request);

        assertNotNull(response);

        assertNotNull(response.getModel());

        assertTrue(
                response.getProbabilityOfDefect() >= 0.0 &&
                        response.getProbabilityOfDefect() <= 1.0
        );

        System.out.println(
                "Prediction: " + response.isPredictedDefect()
        );

        System.out.println(
                "Probability: " + response.getProbabilityOfDefect()
        );

        System.out.println(
                "Model: " + response.getModel()
        );
    }
}
