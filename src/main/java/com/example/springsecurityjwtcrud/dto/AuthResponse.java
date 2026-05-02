package com.example.springsecurityjwtcrud.dto;

public record AuthResponse(String token, String username, String email) {}
