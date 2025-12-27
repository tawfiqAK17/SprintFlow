package com.ensa.SprintFlow.security.service;

import org.springframework.stereotype.Service;

import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.repository.UserRepository;
import com.ensa.SprintFlow.security.model.UserContext;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserContextService {

  UserRepository userRepository;

  public UserContext loadUserByUsername(String username) {
    User user = userRepository.findByUsername(username);
    UserContext userContext = new UserContext(user);
    return userContext;
  }
}
