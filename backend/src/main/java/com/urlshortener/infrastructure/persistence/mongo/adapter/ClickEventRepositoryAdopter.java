package com.urlshortener.infrastructure.persistence.mongo.adapter;

import com.urlshortener.domain.analytics.ClickEvent;
import com.urlshortener.domain.analytics.DailyClickCount;
import com.urlshortener.domain.analytics.ReferrerCount;
import com.urlshortener.domain.port.ClickEventRepository;
import com.urlshortener.infrastructure.persistence.mongo.aggregration.ClickDayAggregration;
import com.urlshortener.infrastructure.persistence.mongo.aggregration.ReferrerAggregation;
import com.urlshortener.infrastructure.persistence.mongo.document.ClickEventDocument;
import com.urlshortener.infrastructure.persistence.mongo.mapper.ClickEventMapper;
import com.urlshortener.infrastructure.persistence.mongo.repository.ClickEventMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ClickEventRepositoryAdopter implements ClickEventRepository {

    private static final String DIRECT_REFERRER = "Direct";

    private final ClickEventMongoRepository mongoRepository;
    private final ClickEventMapper clickEventMapper;
    private final MongoTemplate mongoTemplate;

    @Override
    public ClickEvent save(ClickEvent clickEvent) {
        ClickEventDocument document = clickEventMapper.toDocument(clickEvent);
        ClickEventDocument saved = mongoRepository.save(document);
        return clickEventMapper.toDomain(saved);
    }

    @Override
    public List<DailyClickCount> countClicksByDay(String urlId, Instant since) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("urlId").is(urlId).and("clickedAt").gte(since)),
                Aggregation.project("clickedAt")
                        .andExpression("dateToString('%Y-%m-%d', clickedAt)").as("date"),
                Aggregation.group("date").count().as("clicks"),
                Aggregation.sort(Sort.Direction.ASC, "_id")
        );

        return mongoTemplate.aggregate(aggregation, ClickEventDocument.class, ClickDayAggregration.class)
                .getMappedResults()
                .stream()
                .map(row -> new DailyClickCount(row.getDate(), row.getClicks()))
                .toList();
    }

    @Override
    public List<ReferrerCount> topReferrers(String urlId, int limit) {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("urlId").is(urlId)),
                Aggregation.group("referrer").count().as("clicks"),
                Aggregation.limit(limit)
        );

        return mongoTemplate.aggregate(aggregation, ClickEventDocument.class, ReferrerAggregation.class)
                .getMappedResults()
                .stream()
                .map(row -> new ReferrerCount(formatReferrer(row.getReferrer()), row.getClicks()))
                .toList();
    }

    private String formatReferrer(String referrer) {
        return StringUtils.hasText(referrer) ? referrer.trim() : DIRECT_REFERRER;
    }
}
