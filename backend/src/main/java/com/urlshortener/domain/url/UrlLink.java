package com.urlshortener.domain.url;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlLink {

    private String id;

    private String shortCode;

    private String originalUrl;

    private String ownerId;

    private String title;

    private boolean active;

    @Builder.Default
    private long clickCount = 0;

    private Instant expiresAt;

    private Instant createdAt;

    private Instant updatedAt;
}
