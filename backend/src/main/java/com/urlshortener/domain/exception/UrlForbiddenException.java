package com.urlshortener.domain.exception;

public class UrlForbiddenException extends RuntimeException {

    public UrlForbiddenException() {
        super("You do not have permission to modify this link");
    }
}
