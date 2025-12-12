package com.ensa.SprintFlow.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  @Value("JWT_SECRET_KEY")
  private String jwtSecretKey;

  public <T> T extractClaims(String jwt, Function<Claims, T> claimsResolever) {
    Claims claims =
        Jwts.parser()
            .verifyWith((SecretKey) getSecretKey())
            .build()
            .parseSignedClaims(jwt)
            .getPayload();
    return claimsResolever.apply(claims);
  }

  public String generateToken(UserDetails userDetails) {
    Jwts.builder()
        .subject(userDetails.getUsername())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
        .signWith(getSecretKey())
        .compact();
    return null;
  }

  private Key getSecretKey() {
    byte[] keyBytes = Decoders.BASE64.decode(jwtSecretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
