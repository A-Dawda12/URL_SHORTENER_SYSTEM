package com.urlshortener.application.service;

import com.urlshortener.domain.exception.UrlLinkUnavailableException;
import com.urlshortener.domain.exception.UrlNotFoundException;
import com.urlshortener.domain.port.UrlLinkRepository;
import com.urlshortener.domain.url.UrlLink;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RedirectService {

    private final UrlLinkRepository urlLinkRepository;
    private final UrlCacheService urlCacheService;

    public String resolveOriginalUrl(String shortCode) {
        return urlCacheService.getOriginalUrl(shortCode)
                .orElseGet(() -> loadFromDatabase(shortCode));
    }

    private String loadFromDatabase(String shortCode) {
        if(urlCacheService.isNegativeCacheHit(shortCode)) {
            throw new UrlNotFoundException(shortCode);
        }

        UrlLink urlLink = urlLinkRepository.findByShortCode(shortCode)
                .orElseThrow(() -> {
                    urlCacheService.cacheMiss(shortCode);
                    return new UrlNotFoundException(shortCode);
                });

        validateAvailable(urlLink);

        String originalUrl = urlLink.getOriginalUrl();
        urlCacheService.cacheUrl(shortCode, originalUrl);
        return originalUrl;
    }

    private void validateAvailable(UrlLink urlLink) {
        if(!urlLink.isActive()) {
            throw new UrlLinkUnavailableException(urlLink.getShortCode());
        }
        if(urlLink.getExpiresAt() != null && urlLink.getExpiresAt().isBefore(Instant.now())) {
            throw new UrlLinkUnavailableException(urlLink.getShortCode());
        }
    }
}
