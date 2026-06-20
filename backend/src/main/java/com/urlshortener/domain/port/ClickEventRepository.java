package com.urlshortener.domain.port;

import com.urlshortener.domain.analytics.ClickEvent;

public interface ClickEventRepository {

    ClickEvent save(ClickEvent clickEvent);
}
