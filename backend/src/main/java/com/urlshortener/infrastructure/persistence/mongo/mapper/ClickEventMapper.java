package com.urlshortener.infrastructure.persistence.mongo.mapper;

import com.urlshortener.domain.analytics.ClickEvent;
import com.urlshortener.infrastructure.persistence.mongo.document.ClickEventDocument;
import org.springframework.stereotype.Component;

@Component
public class ClickEventMapper {

    public ClickEvent toDomain(ClickEventDocument document) {
        if(document == null) {
            return null;
        }

        return ClickEvent.builder()
                .id(document.getId())
                .urlId(document.getUrlId())
                .shortCode(document.getShortCode())
                .clickedAt(document.getClickedAt())
                .ipHash(document.getIpHash())
                .userAgent(document.getUserAgent())
                .referrer(document.getReferrer())
                .build();
    }

    public ClickEventDocument toDocument(ClickEvent clickEvent) {
        if (clickEvent == null) {
            return null;
        }

        return ClickEventDocument.builder()
                .id(clickEvent.getId())
                .urlId(clickEvent.getUrlId())
                .shortCode(clickEvent.getShortCode())
                .clickedAt(clickEvent.getClickedAt())
                .ipHash(clickEvent.getIpHash())
                .userAgent(clickEvent.getUserAgent())
                .referrer(clickEvent.getReferrer())
                .build();
    }
}
