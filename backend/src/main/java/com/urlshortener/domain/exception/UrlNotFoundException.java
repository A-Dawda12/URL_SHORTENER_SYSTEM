package com.urlshortener.domain.exception;

public class UrlNotFoundException extends RuntimeException {

    public UrlNotFoundException(String urlId) {
        super("Short link nnot found: " + urlId);
    }
}
