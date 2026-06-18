package com.urlshortener.application.service;

import com.urlshortener.domain.exception.UrlLinkUnavailableException;
import com.urlshortener.domain.exception.UrlNotFoundException;
import com.urlshortener.domain.port.UrlLinkRepoistory;
import com.urlshortener.domain.url.UrlLink;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RedirectService {

    private final UrlLinkRepoistory urlLinkRepoistory;

    public String resolveOriginalUrl(String shortCode) {
        UrlLink urlLink = urlLinkRepoistory.findByShortCode(shortCode)
                .orElseThrow(() -> new UrlNotFoundException(shortCode));

        validateAvailable(urlLink);

        return urlLink.getOriginalUrl();
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
