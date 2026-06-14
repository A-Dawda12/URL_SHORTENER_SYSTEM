package com.urlshortener.infrastructure.persistence.mongo.repository;

import com.urlshortener.infrastructure.persistence.mongo.document.UrlLinkDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UrlLinkMongoRepository extends MongoRepository<UrlLinkDocument, String> {

    Optional<UrlLinkDocument> findByShortCode(String shortCode);

    List<UrlLinkDocument> findByOwnerIdOrderByCreatedAtDesc(String ownerId);

    boolean existsByShortCode(String shortCode);
}
