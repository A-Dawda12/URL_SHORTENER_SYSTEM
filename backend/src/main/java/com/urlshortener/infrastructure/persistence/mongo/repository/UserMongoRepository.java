package com.urlshortener.infrastructure.persistence.mongo.repository;

import com.urlshortener.infrastructure.persistence.mongo.document.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

// Spring talks to MongoDB. Only knows UserDocument, not domain User.
public interface UserMongoRepository extends MongoRepository<UserDocument, String> {

    Optional<UserDocument> findByEmail(String email);

    boolean existsByEmail(String email);
}
