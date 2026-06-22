package com.urlshortener.infrastructure.persistence.mongo.adapter;

import com.urlshortener.domain.port.UrlLinkRepository;
import com.urlshortener.domain.url.UrlLink;
import com.urlshortener.infrastructure.persistence.mongo.document.UrlLinkDocument;
import com.urlshortener.infrastructure.persistence.mongo.mapper.UrlLinkMapper;
import com.urlshortener.infrastructure.persistence.mongo.repository.UrlLinkMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UrlRepositoryAdapter implements UrlLinkRepository {

    private final UrlLinkMongoRepository mongoRepository;
    private final UrlLinkMapper urlLinkMapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public UrlLink save(UrlLink urlLink) {
        UrlLinkDocument document = urlLinkMapper.toDocument(urlLink);
        UrlLinkDocument saved = mongoRepository.save(document);
        return urlLinkMapper.toDomain(saved);
    }

    @Override
    public Optional<UrlLink> findById(String id) {
        return mongoRepository.findById(id).map(urlLinkMapper::toDomain);
    }

    @Override
    public Optional<UrlLink> findByShortCode(String shortCode) {
        return mongoRepository.findByShortCode(shortCode).map(urlLinkMapper::toDomain);
    }

    @Override
    public List<UrlLink> findByOwnerId(String ownerId) {
        return mongoRepository.findByOwnerIdOrderByCreatedAtDesc(ownerId)
                .stream()
                .map(urlLinkMapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByShortCode(String shortCode) {
        return mongoRepository.existsByShortCode(shortCode);
    }

    @Override
    public void deleteById(String id) {
        mongoRepository.deleteById(id);
    }

    @Override
    public void incrementClickCount(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        Update update = new Update().inc("clickCount", 1L);
        mongoTemplate.updateFirst(query, update, UrlLinkDocument.class);
    }

}
