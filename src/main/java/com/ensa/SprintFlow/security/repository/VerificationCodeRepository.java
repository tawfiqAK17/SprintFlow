package com.ensa.SprintFlow.security.repository;

import com.ensa.SprintFlow.security.model.VerificationCode;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
  public VerificationCode findByCode(String code);

  @Modifying
  @Transactional
  public void deleteByExpirationDateBefore(LocalDateTime now);
}
