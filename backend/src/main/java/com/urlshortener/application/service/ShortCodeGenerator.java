package com.urlshortener.application.service;

import com.urlshortener.domain.exception.ShortCodeGenerationException;
import com.urlshortener.domain.port.CounterRepository;
import com.urlshortener.domain.port.UrlLinkRepoistory;
import com.urlshortener.domain.shortcode.CounterNames;
import com.urlshortener.domain.shortcode.ShortCodeEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShortCodeGenerator {

    private static final  int MAX_COLLISION_RETRIES = 5;

    private final CounterRepository counterRepository;
    private final UrlLinkRepoistory urlLinkRepoistory;

    public String generateUniqueShortCode() {
        for(int attempt = 0; attempt < MAX_COLLISION_RETRIES; attempt++) {
            long sequence = counterRepository.getNextSequence(CounterNames.URL_SHORT_CODE);

            String shortCode = ShortCodeEncoder.encode(sequence);

            if(!urlLinkRepoistory.existsByShortCode(shortCode)) {
                return shortCode;
            }
        }
        throw new ShortCodeGenerationException();
    }
}
