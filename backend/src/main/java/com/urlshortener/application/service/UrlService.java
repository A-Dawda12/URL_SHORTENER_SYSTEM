package com.urlshortener.application.service;

import com.urlshortener.api.v1.dto.request.CreateUrlRequest;
import com.urlshortener.api.v1.dto.response.UrlResponse;
import com.urlshortener.config.AppProperties;
import com.urlshortener.domain.port.UrlLinkRepoistory;
import com.urlshortener.domain.url.UrlLink;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlLinkRepoistory urlLinkRepoistory;
    private final ShortCodeGenerator shortCodeGenerator;
    private final AppProperties appProperties;

    public UrlResponse createUrl(String ownerId, CreateUrlRequest request) {
        String shortCode = shortCodeGenerator.generateUniqueShortCode();

        String title = StringUtils.hasText(request.getTitle()) ? request.getTitle().trim() : null;

        UrlLink urlLink = UrlLink.builder()
                .shortCode(shortCode)
                .originalUrl(request.getOriginalUrl().trim())
                .ownerId(ownerId)
                .title(title)
                .active(true)
                .clickCount(0)
                .build();

        UrlLink saved = urlLinkRepoistory.save(urlLink);
        return toResponse(saved);
    }

    private UrlResponse toResponse(UrlLink urlLink) {
        String baseUrl = trimTailingSlash(appProperties.getBaseUrl());
        String shortUrl = baseUrl + "/" + urlLink.getShortCode();

        return new UrlResponse(
                urlLink.getId(),
                urlLink.getShortCode(),
                shortUrl,
                urlLink.getOriginalUrl(),
                urlLink.getTitle(),
                urlLink.getCreatedAt()
        );
    }

    private String trimTailingSlash(String baseUrl) {
        if(baseUrl == null || baseUrl.isBlank()) {
            return "http://localhost:8080";
        }
        return baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

}
