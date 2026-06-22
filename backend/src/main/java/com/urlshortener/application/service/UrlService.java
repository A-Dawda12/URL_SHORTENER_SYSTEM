package com.urlshortener.application.service;

import com.urlshortener.api.v1.dto.request.CreateUrlRequest;
import com.urlshortener.api.v1.dto.response.UrlResponse;
import com.urlshortener.config.AppProperties;
import com.urlshortener.domain.exception.UrlForbiddenException;
import com.urlshortener.domain.exception.UrlNotFoundException;
import com.urlshortener.domain.port.UrlLinkRepository;
import com.urlshortener.domain.url.UrlLink;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlLinkRepository urlLinkRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private final AppProperties appProperties;
    private final UrlCacheService urlCacheService;

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

        UrlLink saved = urlLinkRepository.save(urlLink);
        return toResponse(saved);
    }

    public List<UrlResponse> listUrlsForOwner(String ownerId) {
        return urlLinkRepository.findByOwnerId(ownerId).stream()
                .map(this::toResponse)
                .toList();
    }

    public void deleteUrl(String ownerId, String urlId) {
        UrlLink urlLink = urlLinkRepository.findById(urlId)
                .orElseThrow(() -> new UrlNotFoundException(urlId));

        if(!ownerId.equals(urlLink.getOwnerId())) {
            throw new UrlForbiddenException();
        }
        String shortCode = urlLink.getShortCode();
        urlLinkRepository.deleteById(urlId);
        urlCacheService.evict(shortCode);
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
                urlLink.getClickCount(),
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
