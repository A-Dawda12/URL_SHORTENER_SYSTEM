package com.urlshortener.api.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RegisterResponse {

    private final String userId;
    private final String email;
    private final String displayName;

    public RegisterResponse(String userId, String email, String displayName) {
        this.userId = userId;
        this.email = email;
        this.displayName = displayName;
    }

    public String userId() {
        return userId;
    }

    public String email() {
        return email;
    }

    public String displayName() {
        return displayName;
    }
}
