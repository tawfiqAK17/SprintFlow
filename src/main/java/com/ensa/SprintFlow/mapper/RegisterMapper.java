package com.ensa.SprintFlow.mapper;

import org.springframework.stereotype.Component;

import com.ensa.SprintFlow.dto.request.RegisterRequestDto;
import com.ensa.SprintFlow.model.User;

@Component
public class RegisterMapper {

  public User mapToUser(RegisterRequestDto dto) {
    User user = new User();
    user.setFirstName(dto.getFirstName());
    user.setLastName(dto.getLastName());
    user.setEmail(dto.getEmail());
    user.setUsername(dto.getUsername());
    user.setPassword(dto.getPassword());
    user.setVerified(false);
    return user;
  }
}
