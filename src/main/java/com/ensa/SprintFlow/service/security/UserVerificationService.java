package com.ensa.SprintFlow.service.security;

import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.exception.generalException.ResourceExpiredException;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.model.security.VerificationCode;
import com.ensa.SprintFlow.repository.security.VerificationCodeRepository;
import com.ensa.SprintFlow.service.UserService;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class UserVerificationService {
  VerificationCodeRepository verificationCodeRepository;
  UserService userService;
  EmailService emailService;

  public UserVerificationService(
      VerificationCodeRepository verificationCodeRepository,
      UserService userService,
      EmailService emailService) {
    this.verificationCodeRepository = verificationCodeRepository;
    this.userService = userService;
    this.emailService = emailService;
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
