package com.urlshortener.infrastructure.persistence.mongo.aggregration;

import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
public class ReferrerAggregation {

    @Id
    private String referrer;

    private long clicks;
}
