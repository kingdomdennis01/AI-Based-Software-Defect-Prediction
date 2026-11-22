package com.aidefectprediction.backend.controller;

import com.aidefectprediction.backend.client.MlServiceClient;
import com.aidefectprediction.backend.service.PredictionService;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PredictionControllerHttpTest {

    private MockMvc createMockMvc() throws Exception {

        MlServiceClient client = new MlServiceClient(
                RestClient.builder(),
                "http://127.0.0.1:8000"
        );

        PredictionService service =
                new PredictionService(client);

        PredictionController controller =
                new PredictionController(service);

        LocalValidatorFactoryBean validator =
                new LocalValidatorFactoryBean();

        validator.afterPropertiesSet();

        return MockMvcBuilders
                .standaloneSetup(controller)
                .setValidator(validator)
                .build();
    }

    @Test
    void shouldReturnPredictionThroughHttp() throws Exception {

        MockMvc mockMvc = createMockMvc();

        String requestBody = """
                {
                  "features": {
                    "loc": 50.0,
                    "v(g)": 5.0,
                    "ev(g)": 3.0,
                    "iv(g)": 4.0,
                    "n": 120.0,
                    "v": 600.0,
                    "l": 0.08,
                    "d": 12.0,
                    "i": 45.0,
                    "e": 7200.0,
                    "b": 0.2,
                    "t": 400.0,
                    "lOCode": 45.0,
                    "lOComment": 3.0,
                    "lOBlank": 2.0,
                    "locCodeAndComment": 1.0,
                    "uniq_Op": 15.0,
                    "uniq_Opnd": 20.0,
                    "total_Op": 70.0,
                    "total_Opnd": 50.0,
                    "branchCount": 9.0
                  }
                }
                """;

        MvcResult result = mockMvc.perform(
                        post("/api/predictions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isOk())
                .andReturn();

        String responseBody =
                result.getResponse().getContentAsString();

        assertTrue(responseBody.contains("predicted_defect"));
        assertTrue(responseBody.contains("probability_of_defect"));
        assertTrue(responseBody.contains("model"));

        System.out.println("HTTP Response: " + responseBody);
    }

    @Test
    void shouldRejectEmptyFeatures() throws Exception {

        MockMvc mockMvc = createMockMvc();

        mockMvc.perform(
                        post("/api/predictions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                  "features": {}
                                }
                                """)
                )
                .andExpect(status().isBadRequest());
    }
}
