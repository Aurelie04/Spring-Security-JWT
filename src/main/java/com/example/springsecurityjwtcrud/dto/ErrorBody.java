package com.example.springsecurityjwtcrud.dto;

import java.time.Instant;
import java.util.Map;

public record ErrorBody(int status, String message, Instant timestamp, Map<String, String> fieldErrors) {
    public static ErrorBody simple(int status, String message) {
        return new ErrorBody(status, message, Instant.now(), null);
    }

    public static ErrorBody validation(Map<String, String> fieldErrors) {
        return new ErrorBody(400, "validation failed", Instant.now(), fieldErrors);
    }
}
