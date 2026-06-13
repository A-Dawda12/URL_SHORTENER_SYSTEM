package com.urlshortener;

import com.urlshortener.config.AppProperties;
import com.urlshortener.config.CounterProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.urlshortener.infrastructure.persistence.mongo")
@EnableConfigurationProperties({CounterProperties.class, AppProperties.class})
public class UrlShortenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UrlShortenerApplication.class, args);
    }
}
