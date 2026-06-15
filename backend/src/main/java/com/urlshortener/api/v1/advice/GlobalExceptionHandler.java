package com.urlshortener.api.v1.advice;

import com.urlshortener.api.v1.dto.response.ApiErrorResponse;
import com.urlshortener.api.v1.dto.response.ApiMeta;
import com.urlshortener.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.crypto.ShortBufferException;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Maps exceptions to consistent JSON error responses (LLD §13.2).
 * Runs for all {@code @RestController} classes in the application.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** Invalid {@code @Valid} fields on DTOs (e.g. weak password). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return error(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message);
    }

    /** Duplicate email on register. */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    ResponseEntity<ApiErrorResponse> handleEmailExists(EmailAlreadyExistsException ex) {
        return error(HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS", ex.getMessage());
    }

    /** Wrong email/password on login */
    @ExceptionHandler(InvalidCredentialsException.class)
    ResponseEntity<ApiErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        return error(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", ex.getMessage());
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    ResponseEntity<ApiErrorResponse> handleInvalidRefreshToken(InvalidRefreshTokenException ex) {
        return error(HttpStatus.UNAUTHORIZED, "INVALID_REFRESH_TOKEN", ex.getMessage());
    }

    @ExceptionHandler(ShortBufferException.class)
    ResponseEntity<ApiErrorResponse> handleShortCodeGeneration(ShortCodeGenerationException ex) {
        return error(HttpStatus.SERVICE_UNAVAILABLE, "SHORT_CODE_GENERATION_FAILED", ex.getMessage());
    }

    @ExceptionHandler(UrlNotFoundException.class)
    ResponseEntity<ApiErrorResponse> handleUrlNotFound(UrlNotFoundException ex) {
        return error(HttpStatus.NOT_FOUND, "URL_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(UrlForbiddenException.class)
    ResponseEntity<ApiErrorResponse> handleUrlForbidden(UrlForbiddenException ex) {
        return error(HttpStatus.FORBIDDEN, "URL_FORBIDDEN", ex.getMessage());
    }

    /** Fallback for unexpected errors. */
    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiErrorResponse> handleUnhandled(Exception ex) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "An unexpected error occurred");
    }

    private ResponseEntity<ApiErrorResponse> error(HttpStatus status, String code, String message) {
        ApiMeta meta = new ApiMeta(UUID.randomUUID().toString(), Instant.now());
        ApiErrorResponse body = new ApiErrorResponse(
                false,
                new ApiErrorResponse.ErrorBody(code, message, status.value()),
                meta);
        return ResponseEntity.status(status).body(body);
    }
}
