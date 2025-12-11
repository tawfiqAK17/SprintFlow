package com.ensa.SprintFlow.service;


import com.ensa.SprintFlow.exception.registrationException.userDataIntegrityExeption.DuplicatedUsernameException;
import com.ensa.SprintFlow.exception.registrationException.userDataIntegrityExeption.DuplicatedEmailException;
import com.ensa.SprintFlow.exception.registrationException.RegistrationException;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.repository.UserRepository;
import org.springframework.stereotype.Service;

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

  public void verifyUser(User user) {
    user.setVerified(true);
    userRepository.save(user);
  }
}
