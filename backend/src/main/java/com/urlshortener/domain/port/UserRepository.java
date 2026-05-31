package com.urlshortener.domain.port;

import com.urlshortener.domain.user.User;

import java.util.Optional;

// What AuthService needs: save/find users. No Mongo here — just a promise.
public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
