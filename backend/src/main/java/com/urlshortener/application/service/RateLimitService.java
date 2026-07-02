package com.urlshortener.application.service;

import com.urlshortener.config.AppProperties;
import com.urlshortener.config.RateLimitProperties;
import com.urlshortener.domain.exception.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.HexFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RateLimitService {

    private static final String KEY_PREFIX = "rateLimit:";

    private final StringRedisTemplate redisTemplate;
    private final RateLimitProperties rateLimitProperties;
    private final AppProperties appProperties;

    public void checkRedirect(String clientIp) {
        String key = KEY_PREFIX + "redirect:ip" + hashIp(clientIp);
        enforce(key, rateLimitProperties.getRedirectPerMinute(), Duration.ofMinutes(1));
    }

    private String hashIp(String clientIp) {
        if(!StringUtils.hasText(clientIp)) {
            return "unknown";
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String payload = appProperties.getIpHashSalt() + clientIp.trim();
            byte[] hash = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    private void enforce(String key, int limit, Duration window) {
        Long count = redisTemplate.opsForValue().increment(key);
        if(count == null) {
            return;
        }

        if(count == 1L) {
            redisTemplate.expire(key, window);
        }

        if(count > limit) {
            long retryAfter = resolveRetryAfterSeconds(key, window);
            throw new RateLimitExceededException(retryAfter);
        }
    }

    private long resolveRetryAfterSeconds(String key, Duration window) {
        Long ttl = redisTemplate.getExpire(key);
        if(ttl != null && ttl > 0) {
            return ttl;
        }
        return window.getSeconds();
    }


    public void checkCreate(String clientIp, Optional<String> userId) {
        if(userId.isPresent()) {
            String key = KEY_PREFIX + "create:user:" + userId.get();
            enforce(key, rateLimitProperties.getAuthCreatePerHour(), Duration.ofHours(1));
            return;
        }
        String key = KEY_PREFIX + "create:ip:" + hashIp(clientIp);
        enforce(key, rateLimitProperties.getAnonymousCreatePerHour(), Duration.ofHours(1));
    }

    public void checkApi(String clinetIp, Optional<String> userId) {
        String key = userId
                .map(id -> KEY_PREFIX + "api:user:" + id)
                .orElseGet(() -> KEY_PREFIX + "api:ip:" + hashIp(clinetIp));
        enforce(key, rateLimitProperties.getApiPerMinute(), Duration.ofMinutes(1));
    }
}
