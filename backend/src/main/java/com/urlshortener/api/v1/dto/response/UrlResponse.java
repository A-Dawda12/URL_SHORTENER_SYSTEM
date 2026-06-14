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
    private final Instant createdAt;

    public UrlResponse(
          String urlId,
          String shortCode,
          String shortUrl,
          String originalUrl,
          String title,
          Instant createdAt
    ) {
        this.urlId = urlId;
        this.shortCode = shortCode;
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.title = title;
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

    public Instant createdAt() {
        return createdAt;
    }       

}
