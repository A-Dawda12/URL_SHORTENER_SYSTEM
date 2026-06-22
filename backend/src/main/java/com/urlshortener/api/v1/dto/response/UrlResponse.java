package com.urlshortener.api.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.Instant;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UrlResponse {

    private final String urlId;
    private final String shortCode;
    private final String shortUrl;
    private final String originalUrl;
    private final String title;
    private final long clickCount;
    private final Instant createdAt;

    public UrlResponse(
          String urlId,
          String shortCode,
          String shortUrl,
          String originalUrl,
          String title,
          long clickCount,
          Instant createdAt
    ) {
        this.urlId = urlId;
        this.shortCode = shortCode;
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.title = title;
        this.clickCount = clickCount;
        this.createdAt = createdAt;
    }

    public String urlId() {
        return urlId;
    }

    public String shortCode() {
        return shortCode;
    }
    
    public String shortUrl() {
        return shortUrl;
    }

    public String originalUrl() {
        return originalUrl;
    }
    
    public String title() {
        return title;
    }

    public long clickCount() {
        return clickCount;
    }

    public Instant createdAt() {
        return createdAt;
    }       

}
