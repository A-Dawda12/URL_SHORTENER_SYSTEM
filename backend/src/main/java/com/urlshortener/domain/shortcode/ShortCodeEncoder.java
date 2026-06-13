package com.urlshortener.domain.shortcode;

public class ShortCodeEncoder {

    private static final String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int BASE = ALPHABET.length();

    private ShortCodeEncoder() {

    }

    public static String encode(long value) {
        if(value < 0) {
            throw new IllegalArgumentException("Counter value must be non-negative");
        }
        if(value == 0) {
            return "0";
        }

        StringBuilder encoded = new StringBuilder();
        long remaining = value;
        while(remaining > 0) {
            encoded.append(ALPHABET.charAt((int) (remaining % BASE)));
            remaining /= BASE;
        }
        return encoded.reverse().toString();
    }


}
