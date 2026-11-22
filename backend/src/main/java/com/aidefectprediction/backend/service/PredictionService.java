package com.aidefectprediction.backend.service;

import com.aidefectprediction.backend.client.MlServiceClient;
import com.aidefectprediction.backend.dto.PredictionRequest;
import com.aidefectprediction.backend.dto.PredictionResponse;

import org.springframework.stereotype.Service;

@Service
public class PredictionService {

    private final MlServiceClient mlServiceClient;

    public PredictionService(MlServiceClient mlServiceClient) {
        this.mlServiceClient = mlServiceClient;
    }

    public PredictionResponse predict(PredictionRequest request) {
        return mlServiceClient.predict(request);
    }
}