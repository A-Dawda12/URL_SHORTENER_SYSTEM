package com.urlshortener.infrastructure.persistence.mongo.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "counters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CounterDocument {

    @Id
    private String id;

    private long seq;
}
