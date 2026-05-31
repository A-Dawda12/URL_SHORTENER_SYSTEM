package com.urlshortener.infrastructure.persistence.mongo.adapter;

import com.urlshortener.domain.port.UserRepository;
import com.urlshortener.domain.user.User;
import com.urlshortener.infrastructure.persistence.mongo.document.UserDocument;
import com.urlshortener.infrastructure.persistence.mongo.mapper.UserMapper;
import com.urlshortener.infrastructure.persistence.mongo.repository.UserMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

// Glue: implements UserRepository for AuthService, uses UserMongoRepository + mapper for Mongo.
@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserMongoRepository mongoRepository;
    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        UserDocument document = userMapper.toDocument(user);
        UserDocument saved = mongoRepository.save(document);
        return userMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return mongoRepository.findByEmail(email).map(userMapper::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return mongoRepository.existsByEmail(email);
    }
}
