package com.urlshortener.api.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final ApiMeta meta;

    public ApiResponse(boolean success, T data, ApiMeta meta) {
        this.success = success;
        this.data = data;
        this.meta = meta;
    }

    public static <T> ApiResponse<T> ok(T data, ApiMeta meta) {
        return new ApiResponse<>(true, data, meta);
    }

    public boolean success() {
        return success;
    }

    public T data() {
        return data;
    }

    public ApiMeta meta() {
        return meta;
    }
}
