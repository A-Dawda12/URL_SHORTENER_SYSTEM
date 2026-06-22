package com.urlshortener.infrastructure.persistence.mongo.aggregration;

import lombok.Getter;
import org.springframework.data.annotation.Id;

@Getter
public class ClickDayAggregration {

    @Id
    private String date;

    private long clicks;
}
