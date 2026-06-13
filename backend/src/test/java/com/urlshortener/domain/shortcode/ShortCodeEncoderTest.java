package com.urlshortener.domain.shortcode;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShortCodeEncoderTest {

    @Test
    void encode_mapsSeedCounterToBase62() {
        assertEquals("0", ShortCodeEncoder.encode(0));
        assertEquals("1", ShortCodeEncoder.encode(1));
        assertEquals("10", ShortCodeEncoder.encode(62));
        assertEquals("q0U", ShortCodeEncoder.encode(100_000));
    }
}