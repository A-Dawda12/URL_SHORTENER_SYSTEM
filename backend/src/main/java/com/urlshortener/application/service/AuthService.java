package com.urlshortener.application.service;

import com.urlshortener.api.v1.dto.request.LoginRequest;
import com.urlshortener.api.v1.dto.request.RefreshTokenRequest;
import com.urlshortener.api.v1.dto.request.RegisterRequest;
import com.urlshortener.api.v1.dto.response.AuthResponse;
import com.urlshortener.api.v1.dto.response.RegisterResponse;
import com.urlshortener.domain.exception.EmailAlreadyExistsException;
import com.urlshortener.domain.exception.InvalidCredentialsException;
import com.urlshortener.domain.exception.InvalidRefreshTokenException;
import com.urlshortener.domain.port.UserRepository;
import com.urlshortener.domain.user.User;
import com.urlshortener.domain.user.UserRole;
import com.urlshortener.infrastructure.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthService {


    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public RegisterResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(request.password()))
                .displayName(request.displayName().trim())
                .role(UserRole.USER)
                .build();

        User saved = userRepository.save(user);

        return new RegisterResponse(saved.getId(), saved.getEmail(), saved.getDisplayName());
    }

    public AuthResponse login(LoginRequest request) {
        String email = request.email().trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .filter(u -> passwordEncoder.matches(request.password(), u.getPasswordHash()))
                .orElseThrow(InvalidCredentialsException::new);

        return issueToken(user);
    }

    private AuthResponse issueToken(User user) {
        String accessToken = jwtTokenProvider.createAccessToken(user);
        String refreshToken = createRefreshToken(user.getId());

        user.setRefreshTokenHash(passwordEncoder.encode(refreshToken));
        User saved = userRepository.save(user);

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                jwtTokenProvider.getAccessTokenExpirySeconds(),
                new AuthResponse.AuthUserResponse(
                        saved.getId(),
                        saved.getEmail(),
                        saved.getDisplayName())
        );
    }

    private String createRefreshToken(String userId) {
        long expiryEpoch = Instant.now()
                .plus(jwtTokenProvider.getRefreshTokenExpiryDays(), ChronoUnit.DAYS)
                .getEpochSecond();
        byte[] randomBytes = new byte[32];
        SECURE_RANDOM.nextBytes(randomBytes);
        String randomPart = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        return userId + "." + expiryEpoch + "." + randomPart;

    }

    public AuthResponse refresh(RefreshTokenRequest request) {
        String refreshToken = request.refreshToken();
        String userId = parseRefreshToken(refreshToken);

        User user = userRepository.findById(userId)
                .orElseThrow(InvalidRefreshTokenException::new);

        if(user.getRefreshTokenHash() == null || !passwordEncoder.matches(refreshToken, user.getRefreshTokenHash())) {
            throw new InvalidRefreshTokenException();
        }

        return issueToken(user);

    }

    private String parseRefreshToken(String refreshToken) {
        String[] parts = refreshToken.split("\\.", 3);
        if(parts.length != 3) {
            throw new InvalidRefreshTokenException();
        }
        try {
            long expiryEpoch = Long.parseLong(parts[1]);
            if(Instant.now().getEpochSecond() > expiryEpoch) {
                throw new InvalidRefreshTokenException();
            }
        } catch (NumberFormatException e) {
            throw new InvalidRefreshTokenException();
        }
        return parts[0];
    }
}
