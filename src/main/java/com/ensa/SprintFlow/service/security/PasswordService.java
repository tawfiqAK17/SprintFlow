package com.ensa.SprintFlow.service.security;

import com.ensa.SprintFlow.exception.registrationException.PasswordConditionsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
  PasswordEncoder passwordEncoder;

  public PasswordService(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  public void validateConditions(String password) throws PasswordConditionsException {
    if (password.length() < 8) {
      throw new PasswordConditionsException("the password should be at least 8 characters long");
    }
  }

  public String encode(String password) {
    return passwordEncoder.encode(password);
  }

  public boolean isEquale(String password, String passwordHash) {
    return passwordEncoder.matches(password, passwordHash);
  }
}
