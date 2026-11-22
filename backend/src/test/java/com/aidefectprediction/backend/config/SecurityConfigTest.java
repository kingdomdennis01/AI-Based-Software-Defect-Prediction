package com.aidefectprediction.backend.config;

import com.aidefectprediction.backend.controller.BackendStatusController;
import com.aidefectprediction.backend.controller.PredictionController;
import com.aidefectprediction.backend.dto.PredictionRequest;
import com.aidefectprediction.backend.dto.PredictionResponse;
import com.aidefectprediction.backend.service.PredictionService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        BackendStatusController.class,
        PredictionController.class
})
@Import(SecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PredictionService predictionService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String VALID_REQUEST = """
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

    @Test
    void statusEndpointShouldBePublic() throws Exception {

        mockMvc.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    void predictionShouldRejectUnauthenticatedUser() throws Exception {

        mockMvc.perform(
                        post("/api/predictions")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALID_REQUEST)
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "developer", roles = "USER")
    void predictionShouldRequireCsrfToken() throws Exception {

        mockMvc.perform(
                        post("/api/predictions")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALID_REQUEST)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "developer", roles = "USER")
    void authenticatedUserWithCsrfShouldReceivePrediction()
            throws Exception {

        given(predictionService.predict(any(PredictionRequest.class)))
                .willReturn(
                        new PredictionResponse(
                                true,
                                0.8421,
                                "Logistic Regression"
                        )
                );

        mockMvc.perform(
                        post("/api/predictions")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALID_REQUEST)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.predicted_defect").value(true))
                .andExpect(jsonPath("$.probability_of_defect").value(0.8421))
                .andExpect(jsonPath("$.model").value("Logistic Regression"));
    }

    @Test
    void passwordEncoderShouldHashAndVerifyPassword() {

        String rawPassword = "TestPassword123!";

        String hashedPassword =
                passwordEncoder.encode(rawPassword);

        assertNotEquals(rawPassword, hashedPassword);

        assertTrue(
                passwordEncoder.matches(
                        rawPassword,
                        hashedPassword
                )
        );

        assertFalse(
                passwordEncoder.matches(
                        "WrongPassword123!",
                        hashedPassword
                )
        );
    }
}
