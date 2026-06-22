package com.urlshortener.infrastructure.persistence.mongo.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "click_events")
@CompoundIndex(name = "urlId_clickedAt", def = "{'urlId': 1, 'clickedAt': -1}")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClickEventDocument {

    @Id
    private String id;

    @Indexed
    private String urlId;

    private String shortCode;

    private Instant clickedAt;

    private String ipHash;

    private String userAgent;

    private String referrer;
}
