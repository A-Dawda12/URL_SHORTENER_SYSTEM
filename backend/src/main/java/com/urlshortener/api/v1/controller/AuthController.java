package com.urlshortener.api.v1.controller;

import com.urlshortener.api.v1.dto.request.LoginRequest;
import com.urlshortener.api.v1.dto.request.RefreshTokenRequest;
import com.urlshortener.api.v1.dto.request.RegisterRequest;
import com.urlshortener.api.v1.dto.response.ApiMeta;
import com.urlshortener.api.v1.dto.response.ApiResponse;
import com.urlshortener.api.v1.dto.response.AuthResponse;
import com.urlshortener.api.v1.dto.response.RegisterResponse;
import com.urlshortener.application.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

/**
 * HTTP entry point for auth endpoints (LLD §6.1).
 * Thin layer: validates input, delegates to {@link AuthService}, wraps response.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private ApiMeta meta() {
        return new ApiMeta(UUID.randomUUID().toString(), Instant.now());
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse data = authService.register(request);
        return ApiResponse.ok(data, meta());
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse data = authService.login(request);
        return ApiResponse.ok(data, meta());
    }

    @PostMapping("/refresh")
    public ApiResponse<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse data = authService.refresh(request);
        return ApiResponse.ok(data, meta());
    }
}
