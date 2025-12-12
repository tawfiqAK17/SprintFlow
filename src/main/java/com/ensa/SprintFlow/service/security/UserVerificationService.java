package com.ensa.SprintFlow.service.security;

import com.ensa.SprintFlow.model.security.VerificationCode;
import com.ensa.SprintFlow.repository.security.VerificationCodeRepository;
import com.ensa.SprintFlow.service.UserService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class UserVerificationService {
  VerificationCodeRepository verificationCodeRepository;
  UserService userService;

  public UserVerificationService(
      VerificationCodeRepository verificationCodeRepository, UserService userService) {
    this.verificationCodeRepository = verificationCodeRepository;
    this.userService = userService;
  }

  public void verify(String code) {
    VerificationCode verificationCode = verificationCodeRepository.findByCode(code);
    if (verificationCode == null) {
      // TODO should throw a VerificationCodeIsInvalid
      throw new UnsupportedOperationException("VerificationCodeIsInvalid");
    }

    if (verificationCode.getExpirationDate().isBefore(LocalDateTime.now())) {
      // TODO should throw a VerificationCodeIsInvalid
      throw new UnsupportedOperationException("VerificationCodeIsInvalid");
    }
    userService.verifyUser(verificationCode.getUser());
  }
}
