package com.ensa.SprintFlow.repository.security;

import com.ensa.SprintFlow.model.security.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
  public VerificationCode findByCode(String code);
}
