package com.urlshortener.domain.port;

import com.urlshortener.domain.url.UrlLink;

import java.util.List;
import java.util.Optional;

public interface UrlLinkRepoistory {

    UrlLink save(UrlLink urlLink);

    Optional<UrlLink> findById(String id);

    Optional<UrlLink> findByShortCode(String shortCode);

    List<UrlLink> findByOwnerId(String ownerId);

    boolean existsByShortCode(String shortCode);

    void deleteById(String id);

    void incrementClickCount(String id);
}
