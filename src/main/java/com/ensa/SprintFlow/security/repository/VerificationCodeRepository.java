package com.ensa.SprintFlow.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ensa.SprintFlow.security.model.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
  public VerificationCode findByCode(String code);
}
