package com.urlshortener.domain.exception;

public class UrlLinkUnavailableException extends RuntimeException {

    public UrlLinkUnavailableException(String shortCode) {
        super("Short link is no longer available: " + shortCode);
    }
}
