package com.ensa.SprintFlow.service.security;

import com.ensa.SprintFlow.exception.registrationException.EmailNotValidException;
import com.ensa.SprintFlow.exception.registrationException.RegistrationException;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.service.UserService;

import org.springframework.stereotype.Service;

@Service
public class RegisterService {
  UserService userService;
  PasswordService passwordService;

  public RegisterService(UserService userService, PasswordService passwordService) {
    this.userService = userService;
    this.passwordService = passwordService;
  }

  public void register(User user) throws RegistrationException {
    passwordService.validateConditions(user.getPassword());

    if (!user.getEmail().contains("@")) {
      throw new EmailNotValidException();
    }

    user.setPassword(passwordService.encode(user.getPassword()));

    userService.save(user);
  }
}
