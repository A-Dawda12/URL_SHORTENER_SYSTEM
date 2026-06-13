package com.urlshortener.infrastructure.persistence.mongo.mapper;

import com.urlshortener.domain.url.UrlLink;
import com.urlshortener.infrastructure.persistence.mongo.document.UrlLinkDocument;
import org.springframework.stereotype.Component;

@Component
public class UrlLinkMapper {

    public UrlLink toDomain(UrlLinkDocument document) {
        if(document == null) {
            return null;
        }

        return UrlLink.builder()
                .id(document.getId())
                .shortCode(document.getShortCode())
                .originalUrl(document.getOriginalUrl())
                .ownerId(document.getOwnerId())
                .title(document.getTitle())
                .active(document.isActive())
                .clickCount(document.getClickCount())
                .expiresAt(document.getExpiresAt())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    public UrlLinkDocument toDocument(UrlLink urlLink) {
        if(urlLink == null){
            return null;
        }

        return UrlLinkDocument.builder()
                .id(urlLink.getId())
                .shortCode(urlLink.getShortCode())
                .originalUrl(urlLink.getOriginalUrl())
                .ownerId(urlLink.getOwnerId())
                .title(urlLink.getTitle())
                .active(urlLink.isActive())
                .clickCount(urlLink.getClickCount())
                .expiresAt(urlLink.getExpiresAt())
                .createdAt(urlLink.getCreatedAt())
                .updatedAt(urlLink.getUpdatedAt())
                .build();
    }
}
