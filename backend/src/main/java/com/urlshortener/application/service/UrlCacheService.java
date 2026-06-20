package com.urlshortener.application.service;

import com.urlshortener.config.CacheProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UrlCacheService {

    private static final String URL_KEY_PREFIX = "url:";
    private static final String MISS_KEY_PREFIX = "url:miss:";
    private static final String MISS_VALUE = "1";

    private final StringRedisTemplate redisTemplate;
    private final CacheProperties cacheProperties;

    public Optional<String> getOriginalUrl(String shortCode) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(urlKey(shortCode)));
    }

    public boolean isNegativeCacheHit(String shortCode) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(missKey(shortCode)));
    }

    public void cacheUrl(String shortCode, String originalUrl) {
        redisTemplate.opsForValue().set(
                urlKey(shortCode),
                originalUrl,
                Duration.ofSeconds(cacheProperties.getUrlTtlSeconds())
        );
    }

    public void cacheMiss(String shortCode) {
        redisTemplate.opsForValue().set(
                missKey(shortCode),
                MISS_VALUE,
                Duration.ofSeconds(cacheProperties.getNegativeTtlSeconds())
        );
    }

    private String urlKey(String shortCode) {
        return URL_KEY_PREFIX + shortCode;
    }

    private String missKey(String shortCode) {
        return MISS_KEY_PREFIX + shortCode;
    }
}
