package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.request.RegisterRequestDto;
import com.ensa.SprintFlow.model.User;
import org.springframework.stereotype.Component;

@Component
public class RegisterMapper {

  public User mapToUser(RegisterRequestDto dto) {
    return User.builder()
        .firstName(dto.getFirstName())
        .lastName(dto.getLastName())
        .email(dto.getEmail())
        .username(dto.getUsername())
        .password(dto.getPassword())
        .verified(false)
        .build();
  }
}
