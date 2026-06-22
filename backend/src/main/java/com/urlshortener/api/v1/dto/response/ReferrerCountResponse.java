package com.urlshortener.api.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ReferrerCountResponse {

    private final String referrer;
    private final long clicks;

    public ReferrerCountResponse(String referrer, long clicks) {
        this.referrer = referrer;
        this.clicks = clicks;
    }

    public String referrer() {
        return referrer;
    }

    public long clicks() {
        return clicks;
    }
}
