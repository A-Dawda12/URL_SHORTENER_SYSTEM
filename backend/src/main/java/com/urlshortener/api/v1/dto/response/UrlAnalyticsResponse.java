package com.urlshortener.api.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UrlAnalyticsResponse {

    private final String urlId;
    private final String shortCode;
    private final long totalClick;
    private final List<DailyClickCountResponse> clicksByDay;
    private final List<ReferrerCountResponse> topReferrers;

    public UrlAnalyticsResponse(
            String urlId,
            String shortCode,
            long totalClick,
            List<DailyClickCountResponse> clicksByDay,
            List<ReferrerCountResponse> topReferrers
    ) {
        this.urlId = urlId;
        this.shortCode = shortCode;
        this.totalClick = totalClick;
        this.clicksByDay = clicksByDay;
        this.topReferrers = topReferrers;
    }

    public String urlId() {
        return urlId;
    }

    public String shortCode() {
        return shortCode;
    }

    public long totalClick() {
        return totalClick;
    }

    public List<DailyClickCountResponse> clicksByDay() {
        return clicksByDay;
    }

    public List<ReferrerCountResponse> topReferrers() {
        return topReferrers;
    }
}
