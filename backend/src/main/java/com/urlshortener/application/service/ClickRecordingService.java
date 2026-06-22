package com.urlshortener.application.service;

import com.urlshortener.config.AppProperties;
import com.urlshortener.domain.analytics.ClickEvent;
import com.urlshortener.domain.port.ClickEventRepository;
import com.urlshortener.domain.port.UrlLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class ClickRecordingService {

    private final ClickEventRepository clickEventRepository;
    private final UrlLinkRepository urlLinkRepository;
    private final AppProperties appProperties;


    @Async
    public void recordClick(String shortCode, String clientIp, String userAgent, String referrer) {
        urlLinkRepository.findByShortCode(shortCode).ifPresent(urlLink -> {
            ClickEvent clickEvent = ClickEvent.builder()
                    .urlId(urlLink.getId())
                    .shortCode(urlLink.getShortCode())
                    .clickedAt(Instant.now())
                    .ipHash((hashIp(clientIp)))
                    .userAgent(trimToNull(userAgent))
                    .referrer(trimToNull(referrer))
                    .build();


            clickEventRepository.save(clickEvent);
            urlLinkRepository.incrementClickCount(urlLink.getId());
        });
    }

    private String hashIp(String clientIp) {
        if(!StringUtils.hasText(clientIp)){
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String payload = appProperties.getIpHashSalt() + clientIp.trim();
            byte[] hash = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 NOT AVAILABLE", e);
        }
    }

    private String trimToNull(String value) {
        if(!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }
}
