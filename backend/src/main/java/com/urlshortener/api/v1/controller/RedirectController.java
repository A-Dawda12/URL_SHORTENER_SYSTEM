package com.urlshortener.api.v1.controller;

import com.urlshortener.application.service.RedirectService;
import com.urlshortener.domain.exception.UrlNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
public class RedirectController {

    private static final Pattern SHORT_CODE_PATTERN = Pattern.compile("^[0-9a-zA-Z]{1,12}$");
    private final RedirectService redirectService;

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {
        if(!SHORT_CODE_PATTERN.matcher(shortCode).matches()){
            throw new UrlNotFoundException(shortCode);
        }

        String originalUrl = redirectService.resolveOriginalUrl(shortCode);
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(originalUrl))
                .build();
    }
}
