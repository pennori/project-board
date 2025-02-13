package com.board.api.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    private static final String CLAIM_USERNAME = "username";
    private static final String CLAIM_ROLE = "role";

    private final SecretKey secretKey;
    @Getter
    private final String expireTime;

    public JWTUtil(@Value("${jwt.secret-key}") String secret, @Value("${jwt.exp-time}") String expireTime) {
        this.secretKey = initializeSecretKey(secret);
        this.expireTime = expireTime;
    }

    private SecretKey initializeSecretKey(String secret) {
        return new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String getUsername(String token) {
        return parseToken(token).get(CLAIM_USERNAME, String.class);
    }

    public String getRole(String token) {
        return parseToken(token).get(CLAIM_ROLE, String.class);
    }

    public Boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    public String createJwt(String username, String role, Long expiredMs) {
        Date now = new Date(System.currentTimeMillis());
        Date expiration = new Date(now.getTime() + expiredMs);

        return Jwts.builder()
                .claim(CLAIM_USERNAME, username)
                .claim(CLAIM_ROLE, role)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    private Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}