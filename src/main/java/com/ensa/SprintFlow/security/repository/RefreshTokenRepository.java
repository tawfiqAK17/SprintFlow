package com.ensa.SprintFlow.security.repository;

import com.ensa.SprintFlow.security.model.RefreshToken;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  public RefreshToken findByToken(String refreshToken);

  @Modifying
  @Transactional
  public int deleteByExpirationDateBefore(LocalDateTime now);
}
