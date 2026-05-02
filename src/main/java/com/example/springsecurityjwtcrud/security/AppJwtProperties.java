package com.example.springsecurityjwtcrud.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public class AppJwtProperties {

    /**
     * HMAC signing secret (ASCII). Must be long enough for HS256 (typically 256+ bits / 32+ bytes).
     */
    private String secret = "";

    private long expirationMs = 86400000L;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationMs() {
        return expirationMs;
    }

    public void setExpirationMs(long expirationMs) {
        this.expirationMs = expirationMs;
    }
}
