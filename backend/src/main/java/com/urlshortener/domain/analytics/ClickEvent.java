package com.urlshortener.domain.analytics;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClickEvent {

    private String id;
    private String urlId;
    private String shortCode;
    private Instant clickedAt;
    private String ipHash;
    private String userAgent;
    private String referrer;
}
