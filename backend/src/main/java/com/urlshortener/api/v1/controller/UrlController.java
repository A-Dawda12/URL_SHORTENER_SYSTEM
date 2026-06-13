package com.urlshortener.api.v1.controller;

import com.urlshortener.api.v1.dto.request.CreateUrlRequest;
import com.urlshortener.api.v1.dto.response.ApiMeta;
import com.urlshortener.api.v1.dto.response.ApiResponse;
import com.urlshortener.api.v1.dto.response.UrlResponse;
import com.urlshortener.application.service.UrlService;
import com.urlshortener.infrastructure.security.AuthenticatedUser;
import com.urlshortener.infrastructure.security.SecurityUtils;
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
 * REST API for short links (LLD §6). Step 2.3: create only.
 * Requires JWT — {@link SecurityUtils} reads owner from token.
 */
@RestController
@RequestMapping("/api/v1/urls")
@RequiredArgsConstructor
public class UrlController {

    private final UrlService urlService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse create(@Valid @RequestBody CreateUrlRequest request) {
        AuthenticatedUser user = SecurityUtils.requireAuthenticatedUser();
        UrlResponse data = urlService.createUrl(user.getUserId(), request);
        return ApiResponse.ok(data, meta());
    }

    private ApiMeta meta() {
        return new ApiMeta(UUID.randomUUID().toString(), Instant.now());
    }
}