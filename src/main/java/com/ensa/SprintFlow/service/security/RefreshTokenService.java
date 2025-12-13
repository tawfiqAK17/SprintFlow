package com.ensa.SprintFlow.service.security;

import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.exception.generalException.ResourceExpiredException;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.model.security.RefreshToken;
import com.ensa.SprintFlow.repository.security.RefreshTokenRepository;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenService {
  RefreshTokenRepository refreshTokenRepository;

  RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
  }

  public String generateToken(User user) {
    String refreshTokenString = generateTokenString();
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setUser(user);
    refreshToken.setToken(refreshTokenString);
    refreshToken.setExpirationDate(
        LocalDateTime.now().plusDays(7)); // the refresh token will be valid for 7 days
    refreshTokenRepository.save(refreshToken);
    return refreshTokenString;
  }

  private String generateTokenString() {
    SecureRandom secureRandom = new SecureRandom();
    Base64.Encoder encoder = Base64.getUrlEncoder();
    byte[] randomBytes = new byte[24];
    secureRandom.nextBytes(randomBytes);
    return encoder.encodeToString(randomBytes);
  }

  public User getUser(String refreshToken) {
    RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken);
    if (refreshTokenEntity == null) {
      throw new NotFoundException("the given refresh token was not found");
    }
    if (refreshTokenEntity.getExpirationDate().isBefore(LocalDateTime.now())) {
      throw new ResourceExpiredException("the given refresh token was expired");
    }
    return refreshTokenEntity.getUser();
  }
}
