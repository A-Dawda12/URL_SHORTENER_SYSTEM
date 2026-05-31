package com.urlshortener.infrastructure.persistence.mongo.document;

import com.urlshortener.domain.user.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

/**
 * MongoDB representation of a user (LLD infrastructure/persistence/mongo/document).
 * Maps to collection {@code users}. Annotations here do not belong on domain {@link com.urlshortener.domain.user.User}.
 */
@Document(collection = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDocument {

    @Id
    private String id;

    @Indexed(unique = true, direction = IndexDirection.ASCENDING)
    private String email;

    private String passwordHash;

    private String displayName;

    private UserRole role;

    @CreatedDate
    @Indexed(direction = IndexDirection.DESCENDING)
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
