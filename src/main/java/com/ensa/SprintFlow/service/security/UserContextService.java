package com.ensa.SprintFlow.service.security;

import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.model.security.UserContext;
import com.ensa.SprintFlow.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserContextService {

  UserRepository userRepository;

  public UserContextService(UserRepository repository) {
    userRepository = repository;
  }

  public UserContext loadUserByUsername(String username) {
    User user = userRepository.findByUsername(username);
    UserContext userContext = new UserContext(user);
    return userContext;
  }
}
