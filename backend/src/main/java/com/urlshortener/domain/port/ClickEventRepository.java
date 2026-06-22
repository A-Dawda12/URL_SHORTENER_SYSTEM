package com.urlshortener.domain.port;

import com.urlshortener.domain.analytics.ClickEvent;
import com.urlshortener.domain.analytics.DailyClickCount;
import com.urlshortener.domain.analytics.ReferrerCount;

import java.time.Instant;
import java.util.List;

public interface ClickEventRepository {

    ClickEvent save(ClickEvent clickEvent);

    List<DailyClickCount> countClicksByDay(String urlId, Instant since);

    List<ReferrerCount> topReferrers(String urlId, int limit);

}
