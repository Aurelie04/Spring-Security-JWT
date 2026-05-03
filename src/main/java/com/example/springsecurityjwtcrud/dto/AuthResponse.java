package com.example.springsecurityjwtcrud.dto;

/** Access token response (OAuth2-style metadata for clients). */
public record AuthResponse(String token, String tokenType, long expiresIn, String username, String email) {}
