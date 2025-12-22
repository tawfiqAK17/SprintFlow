package com.ensa.SprintFlow.service.security;

import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.service.UserService;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
