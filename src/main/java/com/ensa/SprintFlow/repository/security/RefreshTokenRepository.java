package com.ensa.SprintFlow.repository.security;

import com.ensa.SprintFlow.model.security.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  public RefreshToken findByToken(String refreshToken);
}
