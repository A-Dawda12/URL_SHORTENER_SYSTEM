package com.urlshortener.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.urlshortener.api.v1.dto.response.ApiErrorResponse;
import com.urlshortener.api.v1.dto.response.ApiMeta;
import com.urlshortener.application.service.RateLimitService;
import com.urlshortener.domain.exception.RateLimitExceededException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private static final Pattern SHORT_CODE_PATTERN = Pattern.compile("^[0-9a-zA-Z]{1,12}$");

    private final RateLimitService rateLimitService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if(shouldSkip(request)){
            filterChain.doFilter(request, response);
            return;
        }

        try {
            applyLimit(request);
            filterChain.doFilter(request, response);
        } catch (RateLimitExceededException ex) {
            writeTooManyRequests(response, ex.getRetryAfterSeconds());
        }
    }

    private boolean shouldSkip(HttpServletRequest request) {
        String path = request.getRequestURI();
        if(path == null) {
            return true;
        }

        if(path.startsWith("/actuator/health") || path.startsWith("/actuator/info")) {
            return true;
        }
        if(HttpMethod.POST.matches(request.getMethod())
                && (path.equals("/api/v1/auth/register")
                || path.equals("api/v1/auth/login")
                || path.equals("api/v1/auth/refresh"))){
            return true;
        }

        return !isPublicRedirect(request) && !isApiRequest(request);
    }

    private boolean isApiRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path != null && path.startsWith("/api/v1/");
    }

    private boolean isPublicRedirect(HttpServletRequest request) {
        if(!HttpMethod.GET.matches(request.getMethod())) {
            return false;
        }

        String uri = request.getRequestURI();
        if(uri == null || uri.length() <= 1 || uri.indexOf('/', 1) != -1) {
            return false;
        }
        String shortCoode = uri.substring(1);
        return SHORT_CODE_PATTERN.matcher(shortCoode).matches();
    }

    private void applyLimit(HttpServletRequest request) {
        String clientIp = request.getRemoteAddr();
        Optional<String> userId = resolveUserId();

        if(isPublicRedirect(request)) {
            rateLimitService.checkRedirect(clientIp);
            return;
        }

        if(isCreateUrl(request)) {
            rateLimitService.checkCreate(clientIp, userId);
            return;
        }

        if(isApiRequest(request)) {
            rateLimitService.checkApi(clientIp, userId);
        }
    }

    private boolean isCreateUrl(HttpServletRequest request) {
        return HttpMethod.POST.matches(request.getMethod())
                && "/api/v1/urls".equals(request.getRequestURI());
    }

    private Optional<String> resolveUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof AuthenticatedUser user) {
            return Optional.of(user.getUserId());
        }
        return Optional.empty();
    }

    private void writeTooManyRequests(HttpServletResponse response,
                                      long retryAfterSeconds) throws IOException {

        ApiMeta meta = new ApiMeta(
                UUID.randomUUID().toString(),
                Instant.now()
        );

        ApiErrorResponse body = new ApiErrorResponse(
                false,
                new ApiErrorResponse.ErrorBody(
                        "RATE_LIMIT_EXCEEDED",
                        "Too many requests. Try again later.",
                        HttpStatus.TOO_MANY_REQUESTS.value()
                ),
                meta
        );

        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader("Retry-After", String.valueOf(retryAfterSeconds));

        objectMapper.writeValue(response.getOutputStream(), body);
    }

}
