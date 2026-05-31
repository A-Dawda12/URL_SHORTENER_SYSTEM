package com.urlshortener.api.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.Instant;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ApiMeta {

    private final String requestId;
    private final Instant timestamp;

    public ApiMeta(String requestId, Instant timestamp) {
        this.requestId = requestId;
        this.timestamp = timestamp;
    }

    public String requestId() {
        return requestId;
    }

    public Instant timestamp() {
        return timestamp;
    }
}
