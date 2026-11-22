package com.aidefectprediction.backend.service;

import com.aidefectprediction.backend.dto.LoginRequest;
import com.aidefectprediction.backend.dto.RegisterRequest;
import com.aidefectprediction.backend.entity.AppUser;
import com.aidefectprediction.backend.repository.AppUserRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            AppUserRepository appUserRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager) {

        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public void register(RegisterRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        if (appUserRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered");
        }

        AppUser newUser = new AppUser();

        newUser.setEmail(email);

        newUser.setPasswordHash(
                passwordEncoder.encode(request.getPassword()));

        newUser.setRole("ROLE_USER");

        appUserRepository.save(newUser);
    }

    public Authentication login(LoginRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase(Locale.ROOT);

        UsernamePasswordAuthenticationToken loginToken =
                UsernamePasswordAuthenticationToken.unauthenticated(
                        email,
                        request.getPassword()
                );

        return authenticationManager.authenticate(loginToken);
    }
}

