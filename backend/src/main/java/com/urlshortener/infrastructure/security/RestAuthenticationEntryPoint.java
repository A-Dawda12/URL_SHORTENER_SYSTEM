package com.urlshortener.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urlshortener.api.v1.dto.response.ApiErrorResponse;
import com.urlshortener.api.v1.dto.response.ApiMeta;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        ApiMeta meta = new ApiMeta(UUID.randomUUID().toString(), Instant.now());
        ApiErrorResponse body = new ApiErrorResponse( false,
                new ApiErrorResponse.ErrorBody(
                        "UNAUTHORIZED",
                        "Authentication required",
                        HttpStatus.UNAUTHORIZED.value()),
                meta);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
