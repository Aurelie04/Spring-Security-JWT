package com.example.springsecurityjwtcrud.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenProvider {

    private static final String USER_ID_CLAIM = "uid";

    private final AppJwtProperties jwtProperties;

    public JwtTokenProvider(AppJwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @PostConstruct
    void validateConfiguration() {
        byte[] bytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        if (bytes.length < 32) {
            throw new IllegalStateException(
                    "app.jwt.secret must be at least 32 bytes (256 bits) for HS256; set JWT_SECRET accordingly.");
        }
    }

    private SecretKey signingKey() {
        byte[] bytes = jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(AuthUserDetails userDetails) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtProperties.getExpirationMs());

        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .subject(userDetails.getUsername())
                .claim(USER_ID_CLAIM, userDetails.getId())
                .issuedAt(now)
                .expiration(exp)
                .signWith(signingKey())
                .compact();
    }

    public String extractUsername(String token) {
        return parseClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            Claims claims = parseClaims(token);
            if (!jwtProperties.getIssuer().equals(claims.getIssuer())) {
                return false;
            }
            if (!claims.getSubject().equals(userDetails.getUsername())) {
                return false;
            }
            Date exp = claims.getExpiration();
            return exp != null && !exp.before(new Date());
        } catch (RuntimeException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().verifyWith(signingKey()).build().parseSignedClaims(token).getPayload();
    }
}
