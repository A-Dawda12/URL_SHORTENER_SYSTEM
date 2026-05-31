package com.urlshortener.domain.exception;

/**
 * Thrown when register is called with an email that is already in the database.
 * Mapped to HTTP 409 by {@link com.urlshortener.api.v1.advice.GlobalExceptionHandler}.
 */
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String email) {
        super("Email already registered: " + email);
    }
}
