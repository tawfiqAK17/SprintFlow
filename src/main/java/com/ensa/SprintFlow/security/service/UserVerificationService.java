package com.ensa.SprintFlow.security.service;

import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.exception.generalException.ResourceExpiredException;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.security.model.VerificationCode;
import com.ensa.SprintFlow.security.repository.VerificationCodeRepository;
import com.ensa.SprintFlow.service.UserService;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserVerificationService {
  VerificationCodeRepository verificationCodeRepository;
  UserService userService;
  EmailService emailService;

  // Runs every day at 2 AM
  @Scheduled(cron = "0 0 2 * * ?")
  public void cleanupExpiredTokens() {
    LocalDateTime now = LocalDateTime.now();
    verificationCodeRepository.deleteByExpirationDateBefore(now);
  }

  public void verify(String code) {
    VerificationCode verificationCode = verificationCodeRepository.findByCode(code);
    if (verificationCode == null) {
      throw new NotFoundException("the given verification code was not found");
    }

    if (verificationCode.getExpirationDate().isBefore(LocalDateTime.now())) {
      throw new ResourceExpiredException("the give verification code was expired");
    }
    userService.verifyUser(verificationCode.getUser());
  }

  public void resendVerificationCode(String username) {
    User user = userService.findByUsername(username);
    if (user == null) {
      throw new NotFoundException("no user found with the given user name");
    }
    emailService.sendVerificationEmail(user);
  }
}
