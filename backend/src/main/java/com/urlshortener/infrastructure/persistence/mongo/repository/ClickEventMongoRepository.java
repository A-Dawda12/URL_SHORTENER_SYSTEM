package com.urlshortener.infrastructure.persistence.mongo.repository;

import com.urlshortener.infrastructure.persistence.mongo.document.ClickEventDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClickEventMongoRepository extends MongoRepository<ClickEventDocument, String> {
}
