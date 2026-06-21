package com.urlshortener.application.service;

import com.urlshortener.api.v1.dto.response.DailyClickCountResponse;
import com.urlshortener.api.v1.dto.response.ReferrerCountResponse;
import com.urlshortener.api.v1.dto.response.UrlAnalyticsResponse;
import com.urlshortener.domain.analytics.DailyClickCount;
import com.urlshortener.domain.analytics.ReferrerCount;
import com.urlshortener.domain.exception.UrlForbiddenException;
import com.urlshortener.domain.exception.UrlNotFoundException;
import com.urlshortener.domain.port.ClickEventRepository;
import com.urlshortener.domain.port.UrlLinkRepository;
import com.urlshortener.domain.url.UrlLink;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * AnalyticsService
 *
 * Purpose:
 * Provides analytics for a URL owner.
 *
 * Returns:
 * 1. Total click count.
 * 2. Click counts grouped by day for the last 30 days.
 * 3. Top 10 referrers.
 *
 * Flow:
 *
 * Controller
 *      ↓
 * AnalyticsService
 *      ↓
 * requireOwnedUrl()
 *      ↓
 * ClickEventRepository
 *      ↓
 * MongoDB
 *      ↓
 * Domain Objects
 *      ↓
 * Response DTOs
 *      ↓
 * JSON Response
 */
@Service
@RequiredArgsConstructor
public class AnalyticsService {

    /**
     * Look back period for daily analytics.
     */
    private static final int DEFAULT_DAYS = 30;

    /**
     * Maximum number of top referrers returned.
     */
    private static final int TOP_REFERRERS_LIMIT = 10;

    private final UrlLinkRepository urlLinkRepository;
    private final ClickEventRepository clickEventRepository;

    /**
     * Fetch analytics for a URL.
     *
     * Steps:
     *
     * 1. Verify URL exists and belongs to the owner.
     * 2. Calculate date = now - 30 days.
     * 3. Fetch click counts grouped by day.
     * 4. Convert domain objects into response DTOs.
     * 5. Fetch top referrers.
     * 6. Convert domain objects into response DTOs.
     * 7. Build UrlAnalyticsResponse.
     *
     * Example:
     *
     * {
     *   urlId : "123",
     *   shortCode : "abc",
     *   clickCount : 250,
     *   clicksByDay : [...],
     *   topReferrers : [...]
     * }
     */
    public UrlAnalyticsResponse getAnalytics(String ownerId, String urlId) {

        // Verify URL ownership.
        UrlLink urlLink = requireOwnedUrl(ownerId, urlId);

        // Current time minus 30 days.
        Instant since = Instant.now()
                .minus(DEFAULT_DAYS, ChronoUnit.DAYS);

        /*
         * Fetch click counts grouped by day.
         *
         * Example:
         *
         * [
         *   ("2026-06-19",40),
         *   ("2026-06-20",55)
         * ]
         */
        List<DailyClickCountResponse> clicksByDay =
                clickEventRepository.countClicksByDay(urlId, since)
                        .stream()
                        .map(this::toDailyResponse)
                        .toList();

        /*
         * Fetch top referrers.
         *
         * Example:
         *
         * [
         *   ("google.com",120),
         *   ("twitter.com",70)
         * ]
         */
        List<ReferrerCountResponse> topReferrers =
                clickEventRepository.topReferrers(urlId, TOP_REFERRERS_LIMIT)
                        .stream()
                        .map(this::toReferrerResponse)
                        .toList();

        /*
         * Build final response.
         */
        return new UrlAnalyticsResponse(
                urlLink.getId(),
                urlLink.getShortCode(),
                urlLink.getClickCount(),
                clicksByDay,
                topReferrers
        );
    }

    /**
     * Verify that:
     *
     * 1. URL exists.
     * 2. URL belongs to the current owner.
     *
     * Throws:
     *
     * UrlNotFoundException
     *      if URL does not exist.
     *
     * UrlForbiddenException
     *      if URL belongs to another user.
     */
    private UrlLink requireOwnedUrl(String ownerId, String urlId) {

        UrlLink urlLink = urlLinkRepository.findById(urlId)
                .orElseThrow(() -> new UrlNotFoundException(urlId));

        if (!ownerId.equals(urlLink.getOwnerId())) {
            throw new UrlForbiddenException();
        }

        return urlLink;
    }

    /**
     * Convert Domain object → Response DTO.
     *
     * Example:
     *
     * DailyClickCount("2026-06-20",25)
     *
     * becomes
     *
     * DailyClickCountResponse("2026-06-20",25)
     */
    private DailyClickCountResponse toDailyResponse(
            DailyClickCount dailyClickCount) {

        return new DailyClickCountResponse(
                dailyClickCount.date(),
                dailyClickCount.clicks()
        );
    }

    /**
     * Convert Domain object → Response DTO.
     *
     * Example:
     *
     * ReferrerCount("google.com",150)
     *
     * becomes
     *
     * ReferrerCountResponse("google.com",150)
     */
    private ReferrerCountResponse toReferrerResponse(
            ReferrerCount referrerCount) {

        return new ReferrerCountResponse(
                referrerCount.referrer(),
                referrerCount.clicks()
        );
    }
}