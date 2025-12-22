package com.ensa.SprintFlow.service.security;

import com.ensa.SprintFlow.exception.generalException.PasswordConditionsException;
import java.util.ArrayList;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PasswordService {
  PasswordEncoder passwordEncoder;

  public void validateConditions(String password) throws PasswordConditionsException {
    if (password.length() < 8) {
      throw new PasswordConditionsException("the password should be at least 8 characters long");
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
