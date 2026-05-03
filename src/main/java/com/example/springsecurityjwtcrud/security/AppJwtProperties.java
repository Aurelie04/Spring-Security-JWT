package com.example.springsecurityjwtcrud.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
public class AppJwtProperties {

    /**
     * HMAC signing secret (ASCII). Must be long enough for HS256 (typically 256+ bits / 32+ bytes).
     */
    private String secret = "";

    private long expirationMs = 86400000L;

    /** Issuer (`iss`) claim — validated on every request. */
    private String issuer = "spring-security-jwt-crud";

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

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
}
