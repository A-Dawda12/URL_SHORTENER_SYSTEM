package com.urlshortener.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * Core user entity for business logic (LLD domain layer).
 * No Mongo/Spring annotations — persistence shape is {@link com.urlshortener.infrastructure.persistence.mongo.document.UserDocument}.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String id;
    private String email;
    /** BCrypt hash only; never return in API responses. */
    private String passwordHash;
    private String displayName;
    private UserRole role;
    /** BCrypt of refresh token, null until first Login */
    private String refreshTokenHash;
    private Instant createdAt;
    private Instant updatedAt;
}
