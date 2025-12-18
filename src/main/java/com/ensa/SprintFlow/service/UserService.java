package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.exception.generalException.DataIntegrityViolationException;
import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  UserRepository userRepository;

  UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User save(User user) {
    if (userRepository.existsByUsername(user.getUsername())) {
      throw new DataIntegrityViolationException("username already exists");
    }
    if (userRepository.existsByEmail(user.getEmail())) {
      throw new DataIntegrityViolationException("email already exists");
    }
    return userRepository.save(user);
  }

  public void verifyUser(User user) {
    user.setVerified(true);
    userRepository.save(user);
  }

  public User findByUsername(String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new NotFoundException("no user found with the given username");
    }
    return user;
  }
}
