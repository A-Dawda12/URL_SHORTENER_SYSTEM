package com.urlshortener.api.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class DailyClickCountResponse {

    private final String date;
    private final long clicks;

    public DailyClickCountResponse(String date, long clicks) {
        this.date = date;
        this.clicks = clicks;
    }

    public String date() {
        return date;
    }

    public long clicks() {
        return clicks;
    }
}
