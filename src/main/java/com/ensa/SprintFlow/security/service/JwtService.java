package com.ensa.SprintFlow.security.service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ensa.SprintFlow.security.model.UserContext;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

  @Value("${JWT_SECRET_KEY}")
  private String jwtSecretKey;

  public String generateToken(UserContext userContext) {
    String jwt =
        Jwts.builder()
            .subject(userContext.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(getSecretKey())
            .compact();
    return jwt;
  }

  public <T> T extractClaims(String jwt, Function<Claims, T> claimsResolever) {
    Claims claims =
        Jwts.parser()
            .verifyWith((SecretKey) getSecretKey())
            .build()
            .parseSignedClaims(jwt)
            .getPayload();
    return claimsResolever.apply(claims);
  }

  private Key getSecretKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public String extractUsername(String jwt) {
    return extractClaims(jwt, Claims::getSubject);
  }

  private Date extractExpirationDate(String jwt) {
    return extractClaims(jwt, Claims::getExpiration);
  }

  public boolean isExpired(String jwt) {
    return extractExpirationDate(jwt).before(new Date());
  }
}
