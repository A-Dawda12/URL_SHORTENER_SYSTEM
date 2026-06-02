package com.urlshortener.infrastructure.security;

import com.urlshortener.config.JwtProperties;
import com.urlshortener.domain.user.User;
import com.urlshortener.domain.user.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtTokenProvider {

    private static final String CLAIM_EMAIL = "email";
    private static final String CLAIM_ROLE = "role";
    private static final String CLAIN_TYPE = "type";
    private static final String TOKEN_TYPE_ACCESS = "access";

    private final JwtProperties jwtProperties;;
    private final SecretKey secretKey;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(User user) {
        // Implementation for creating access token
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(jwtProperties.getAccessTokenExpiryMinutes() * 60L);

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getId())
                .claim(CLAIM_EMAIL, user.getEmail())
                .claim(CLAIM_ROLE, user.getRole().name())
                .claim(CLAIN_TYPE, TOKEN_TYPE_ACCESS)
                .issuedAt((Date.from(now)))
                .expiration(Date.from(expiry))
                .signWith(secretKey)
                .compact();
    }

    public int getAccessTokenExpirySeconds() {
        return jwtProperties.getAccessTokenExpiryMinutes() * 60;
    }

    public int getRefreshTokenExpiryDays() {
        return jwtProperties.getRefreshTokenExpiryDays();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Claims parseAccessToken(String token) {
        Claims claims = parseToken(token);
        if(!TOKEN_TYPE_ACCESS.equals(claims.get(CLAIN_TYPE, String.class))) {
            throw new JwtException("Invalid token type");
        }
        return claims;
    }

    public String getUserId(Claims claims) {
        return claims.getSubject();
    }

    public String getEmail(Claims claims) {
        return claims.get(CLAIM_EMAIL, String.class);
    }

    public UserRole getRole(Claims claims) {
        return UserRole.valueOf(claims.get(CLAIM_ROLE, String.class));
    }

}
