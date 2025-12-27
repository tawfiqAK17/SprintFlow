package com.ensa.SprintFlow.security.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.service.UserService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegisterService {
  UserService userService;
  PasswordService passwordService;
  EmailService emailService;

  public void register(User user) {

    passwordService.validateConditions(user.getPassword());
    emailService.validateConditions(user.getEmail());
    user.setPassword(passwordService.encode(user.getPassword()));
    user.setEnrollDate(LocalDateTime.now());
    user = userService.save(user);
    emailService.sendVerificationEmail(user);
  }
}
