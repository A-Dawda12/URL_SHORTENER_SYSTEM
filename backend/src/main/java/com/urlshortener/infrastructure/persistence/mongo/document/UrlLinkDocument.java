package com.urlshortener.infrastructure.persistence.mongo.document;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "urls")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlLinkDocument {

    @Id
    private String id;

    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    private String shortCode;

    private String originalUrl;

    @Indexed(direction = IndexDirection.ASCENDING)
    private String ownerId;

    private String title;

    private boolean active;

    @Builder.Default
    private long clickCount = 0;

    private Instant expiresAt;

    @CreatedDate
    @Indexed(direction = IndexDirection.DESCENDING)
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

}
