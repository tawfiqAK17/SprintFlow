package com.ensa.SprintFlow.service.security;

import com.ensa.SprintFlow.exception.registrationException.RegistrationException;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.service.UserService;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

@Service
public class RegisterService {
  UserService userService;
  PasswordService passwordService;
  EmailService emailService;

  public RegisterService(
      UserService userService, PasswordService passwordService, EmailService emailService) {

    this.userService = userService;
    this.passwordService = passwordService;
    this.emailService = emailService;
  }

  public void register(User user) throws RegistrationException {

    passwordService.validateConditions(user.getPassword());
    emailService.validateConditions(user.getEmail());
    user.setPassword(passwordService.encode(user.getPassword()));
    user.setEnrollDate(LocalDateTime.now());
    user = userService.save(user);
    emailService.sendVerificationEmail(user);
  }

  public ArrayList<String> getPasswordConditions() {
    return passwordService.getConditions();
  }

  public ArrayList<String> getEmailConditions() {
    return emailService.getConditions();
  }
}
