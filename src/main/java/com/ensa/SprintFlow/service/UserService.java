package com.ensa.SprintFlow.service;

import org.springframework.stereotype.Service;

import com.ensa.SprintFlow.exception.registrationException.DuplicatedEmailException;
import com.ensa.SprintFlow.exception.registrationException.DuplicatedUsernameException;
import com.ensa.SprintFlow.exception.registrationException.RegistrationException;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.repository.UserRepository;

@Service
public class UserService {
  UserRepository userRepository;

  UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User save(User user) throws RegistrationException {
    if (userRepository.existsByUsername(user.getUsername())) {
      throw new DuplicatedUsernameException();
    }
    if (userRepository.existsByEmail(user.getEmail())) {
      throw new DuplicatedEmailException();
    }
    return userRepository.save(user);
  }
}
