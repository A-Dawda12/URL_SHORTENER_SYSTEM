package com.urlshortener.infrastructure.persistence.mongo.adapter;


import com.urlshortener.config.CounterProperties;
import com.urlshortener.domain.port.CounterRepository;
import com.urlshortener.domain.shortcode.CounterNames;
import com.urlshortener.infrastructure.persistence.mongo.document.CounterDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CounterRepositoryAdopter implements CounterRepository, ApplicationRunner {

    private final MongoTemplate mongoTemplate;
    private final CounterProperties counterProperties;

    @Override
    public void run(ApplicationArguments args) {
        seedCounterIfMissing(CounterNames.URL_SHORT_CODE);
    }

    @Override
    public long getNextSequence(String counterName) {
        Query query = Query.query(Criteria.where("_id").is(counterName));
        Update update = new Update().inc("seq", 1L);
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(false);

        CounterDocument updated = mongoTemplate.findAndModify(query, update, options, CounterDocument.class);

        if (updated == null) {
            seedCounterIfMissing(counterName);
            updated = mongoTemplate.findAndModify(query, update, options, CounterDocument.class);
        }
        if (updated == null) {
            throw new IllegalStateException("Counter not initialized: " + counterName);
        }
        return updated.getSeq();
    }

    private void seedCounterIfMissing(String counterName) {
        Query existQuery = Query.query(Criteria.where("_id").is(counterName));
        if(mongoTemplate.exists(existQuery, CounterDocument.class)) {
            return;
        }
        long startValue = counterProperties.getInitialSeed() - 1;
        mongoTemplate.insert(new CounterDocument(counterName, startValue));
    }
}
