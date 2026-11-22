package com.aidefectprediction.backend.controller;

import com.aidefectprediction.backend.dto.LoginRequest;
import com.aidefectprediction.backend.dto.RegisterRequest;
import com.aidefectprediction.backend.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final SecurityContextRepository securityContextRepository;
    private final CsrfTokenRepository csrfTokenRepository;

    public AuthController(
            AuthService authService,
            SecurityContextRepository securityContextRepository,
            CsrfTokenRepository csrfTokenRepository) {

        this.authService = authService;
        this.securityContextRepository = securityContextRepository;
        this.csrfTokenRepository = csrfTokenRepository;
    }

    // CSRF TOKEN ENDPOINT
    @GetMapping("/csrf")
    public CsrfToken csrf(CsrfToken csrfToken) {
        return csrfToken;
    }

    // USER REGISTRATION
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(
            @Valid @RequestBody RegisterRequest request) {

        try {
            authService.register(request);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message",
                            "Registration successful"
                    ));

        } catch (IllegalArgumentException exception) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "message",
                            exception.getMessage()
                    ));
        }
    }

    // USER LOGIN
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {

        try {

            Authentication authentication =
                    authService.login(request);

            // Create or retrieve the user's session
            HttpSession session = httpRequest.getSession(true);

            // Set session inactivity timeout to 30 minutes
            session.setMaxInactiveInterval(30 * 60);

            // Change session ID after successful authentication
            httpRequest.changeSessionId();

            // Create a new SecurityContext
            SecurityContext securityContext =
                    SecurityContextHolder.createEmptyContext();

            securityContext.setAuthentication(authentication);

            // Set the authenticated user in SecurityContextHolder
            SecurityContextHolder.setContext(securityContext);

            // Save authentication in the HTTP session
            securityContextRepository.saveContext(
                    securityContext,
                    httpRequest,
                    httpResponse
            );

            // Clear the old CSRF token after successful login
            csrfTokenRepository.saveToken(
                    null,
                    httpRequest,
                    httpResponse
            );

            return ResponseEntity
                    .ok(Map.of(
                            "message",
                            "Login successful"
                    ));

        } catch (AuthenticationException exception) {

            SecurityContextHolder.clearContext();

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "message",
                            "Invalid email or password"
                    ));
        }
    }
}

