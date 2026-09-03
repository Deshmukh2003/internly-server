package com.internly.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  private final SecretKey key;
  private final long expirationMs;

  public JwtService(
    @Value("${security.jwt.secret}") String secret,
    @Value("${security.jwt.expiration-ms}") long expirationMs
  ) {
    if (secret.length() < 32) throw new IllegalArgumentException(
      "JWT secret must be at least 32 characters"
    );
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.expirationMs = expirationMs;
  }

  public String generate(String email, String role) {
    Date now = new Date();
    return Jwts.builder()
      .subject(email)
      .claim("role", role)
      .issuedAt(now)
      .expiration(new Date(now.getTime() + expirationMs))
      .signWith(key)
      .compact();
  }

  public String subject(String token) {
    return parse(token).getPayload().getSubject();
  }

  public boolean valid(String token) {
    try {
      parse(token);
      return true;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

  private Jws<Claims> parse(String token) {
    return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
  }
}
