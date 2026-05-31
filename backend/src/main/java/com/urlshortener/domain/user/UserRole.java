package com.urlshortener.domain.user;

/** Authorization role stored on the user; used later with JWT claims (LLD §9.3). */
public enum UserRole {
    USER,
    ADMIN
}
