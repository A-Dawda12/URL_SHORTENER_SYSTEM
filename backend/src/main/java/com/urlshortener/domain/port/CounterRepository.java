package com.urlshortener.domain.port;

public interface CounterRepository {

    long getNextSequence(String counterName);
}
