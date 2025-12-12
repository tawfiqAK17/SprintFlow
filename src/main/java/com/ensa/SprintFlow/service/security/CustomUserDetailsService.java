package com.ensa.SprintFlow.service.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.repository.UserRepository;
import com.ensa.SprintFlow.model.security.CustomUserDetails;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  UserRepository userRepository;

  public CustomUserDetailsService(UserRepository repository) {
    userRepository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    UserDetails userDetails = new CustomUserDetails(user);
    return userDetails;
  }
}
