package com.urlshortener.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.rate-limit")
public class RateLimitProperties {

    private int anonymousCreatePerHour = 10;

    private int authCreatePerHour = 100;

    private int apiPerMinute = 100;

    private int redirectPerMinute = 1000;

}
