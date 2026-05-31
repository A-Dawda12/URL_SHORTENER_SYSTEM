package com.urlshortener.application.service;

import com.urlshortener.api.v1.dto.request.RegisterRequest;
import com.urlshortener.api.v1.dto.response.RegisterResponse;
import com.urlshortener.domain.exception.EmailAlreadyExistsException;
import com.urlshortener.domain.port.UserRepository;
import com.urlshortener.domain.user.User;
import com.urlshortener.domain.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    // Uses UserRepository only — not UserMongoRepository.
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
}
