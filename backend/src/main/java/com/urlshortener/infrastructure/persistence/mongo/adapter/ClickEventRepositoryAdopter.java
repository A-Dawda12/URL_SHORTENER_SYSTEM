package com.urlshortener.infrastructure.persistence.mongo.adapter;

import com.urlshortener.domain.analytics.ClickEvent;
import com.urlshortener.domain.port.ClickEventRepository;
import com.urlshortener.infrastructure.persistence.mongo.document.ClickEventDocument;
import com.urlshortener.infrastructure.persistence.mongo.mapper.ClickEventMapper;
import com.urlshortener.infrastructure.persistence.mongo.repository.ClickEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClickEventRepositoryAdopter implements ClickEventRepository {

    private final ClickEventMongoRepository mongoRepository;
    private final ClickEventMapper clickEventMapper;

    @Override
    public ClickEvent save(ClickEvent clickEvent) {
        ClickEventDocument document = clickEventMapper.toDocument(clickEvent);
        ClickEventDocument saved = mongoRepository.save(document);
        return clickEventMapper.toDomain(saved);
    }
}
