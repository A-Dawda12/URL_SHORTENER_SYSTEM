package com.urlshortener.api.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AuthResponse {

    private final String accessToken;
    private final String refreshToken;
    private final String tokenType;
    private final int expiresIn;
    private final AuthUserResponse user;


    public AuthResponse(String accessToken, String refreshToken, String tokenType, int expiresIn, AuthUserResponse user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.user = user;
    }

    public String accessToken() {
        return accessToken;
    }

    public String refreshToken() {
        return refreshToken;
    }

    public String tokenType() {
        return tokenType;
    }
    public int expiresIn() {
        return expiresIn;
    }
    public AuthUserResponse user() {
        return user;
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class AuthUserResponse {
        private final String id;
        private final String email;
        private final String displayName;

        public AuthUserResponse(String id, String email, String displayName) {
            this.id = id;
            this.email = email;
            this.displayName = displayName;
        }

        public String id() {
            return id;
        }

        public String email() {
            return email;
        }

        public String displayName() {
            return displayName;
        }
    }
}
