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
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
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

    @GetMapping
    public ApiResponse<List<UrlResponse>> list() {
        AuthenticatedUser user = SecurityUtils.requireAuthenticatedUser();
        List<UrlResponse> data = urlService.listUrlsForOwner(user.getUserId());
        return ApiResponse.ok(data, meta());
    }

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