package com.urlshortener.domain.exception;

public class ShortCodeGenerationException extends RuntimeException {

    public ShortCodeGenerationException() {
        super("Uable to generate a unique short code");
    }
}
