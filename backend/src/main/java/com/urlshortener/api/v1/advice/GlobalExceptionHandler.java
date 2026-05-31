package com.urlshortener.api.v1.advice;

import com.urlshortener.api.v1.dto.response.ApiErrorResponse;
import com.urlshortener.api.v1.dto.response.ApiMeta;
import com.urlshortener.domain.exception.EmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
