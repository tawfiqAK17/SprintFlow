package com.ensa.SprintFlow.service.security;

import java.util.ArrayList;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ensa.SprintFlow.exception.generalException.PasswordConditionsException;

@Service
public class PasswordService {
  PasswordEncoder passwordEncoder;

  public PasswordService(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  public void validateConditions(String password) throws PasswordConditionsException {
    if (password.length() < 8) {
      throw new PasswordConditionsException();
    }
  }

  public String encode(String password) {
    return passwordEncoder.encode(password);
  }

  public boolean match(String password, String passwordHash) {
    return passwordEncoder.matches(password, passwordHash);
  }

  public static ArrayList<String> getConditions() {
    ArrayList<String> details = new ArrayList<>();
    details.add("the password should be at least 8 character long");
    return details;
  }
}
