package com.urlshortener.infrastructure.persistence.mongo.mapper;

import com.urlshortener.domain.user.User;
import com.urlshortener.infrastructure.persistence.mongo.document.UserDocument;
import org.springframework.stereotype.Component;

// Copy fields: User (app) <-> UserDocument (Mongo row).
@Component
public class UserMapper {

    public User toDomain(UserDocument document) {
        if (document == null) {
            return null;
        }
        return User.builder()
                .id(document.getId())
                .email(document.getEmail())
                .passwordHash(document.getPasswordHash())
                .displayName(document.getDisplayName())
                .role(document.getRole())
                .refreshTokenHash(document.getRefreshTokenHash())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    public UserDocument toDocument(User user) {
        if (user == null) {
            return null;
        }
        return UserDocument.builder()
                .id(user.getId())
                .email(user.getEmail())
                .passwordHash(user.getPasswordHash())
                .displayName(user.getDisplayName())
                .role(user.getRole())
                .refreshTokenHash(user.getRefreshTokenHash())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
