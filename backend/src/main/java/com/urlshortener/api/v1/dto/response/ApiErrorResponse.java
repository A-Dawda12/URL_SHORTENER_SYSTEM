package com.urlshortener.api.v1.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ApiErrorResponse {

    private final boolean success;
    private final ErrorBody error;
    private final ApiMeta meta;

    public ApiErrorResponse(boolean success, ErrorBody error, ApiMeta meta) {
        this.success = success;
        this.error = error;
        this.meta = meta;
    }

    public boolean success() {
        return success;
    }

    public ErrorBody error() {
        return error;
    }

    public ApiMeta meta() {
        return meta;
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class ErrorBody {

        private final String code;
        private final String message;
        private final int status;

        public ErrorBody(String code, String message, int status) {
            this.code = code;
            this.message = message;
            this.status = status;
        }

        public String code() {
            return code;
        }

        public String message() {
            return message;
        }

        public int status() {
            return status;
        }
    }
}
