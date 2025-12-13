package com.ensa.SprintFlow.service.security;

import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.model.security.CustomUserDetails;
import com.ensa.SprintFlow.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  UserRepository userRepository;

  public CustomUserDetailsService(UserRepository repository) {
    userRepository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    User user = userRepository.findByUsername(username);
    UserDetails userDetails = new CustomUserDetails(user);
    return userDetails;
  }
}
