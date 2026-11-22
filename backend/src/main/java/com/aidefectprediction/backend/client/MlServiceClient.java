package com.aidefectprediction.backend.client;

import com.aidefectprediction.backend.dto.PredictionRequest;
import com.aidefectprediction.backend.dto.PredictionResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class MlServiceClient {

    private final RestClient restClient;

    public MlServiceClient(
            RestClient.Builder restClientBuilder,
            @Value("${ml.service.base-url:http://127.0.0.1:8000}")
            String mlServiceBaseUrl) {

        this.restClient = restClientBuilder
                .baseUrl(mlServiceBaseUrl)
                .build();
    }

    public PredictionResponse predict(PredictionRequest request) {

        return restClient.post()
                .uri("/predict")
                .body(request)
                .retrieve()
                .body(PredictionResponse.class);
    }
}