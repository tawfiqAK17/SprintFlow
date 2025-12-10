package com.ensa.SprintFlow.mapper;

import org.springframework.stereotype.Component;

import com.ensa.SprintFlow.dto.request.RegisterRequestDto;
import com.ensa.SprintFlow.model.User;

@Component
public class RegisterMapper implements Mapper<User, RegisterRequestDto, RegisterRequestDto> {

  @Override
  public RegisterRequestDto mapToDto(User entity) {
	// TODO Auto-generated method stub
	throw new UnsupportedOperationException("Unimplemented method 'mapToDto'");
  }

  @Override
  public User mapToEntity(RegisterRequestDto dto) {
    User user = new User();
    user.setFirstName(dto.getFirstName());
    user.setLastName(dto.getLastName());
    user.setEmail(dto.getEmail());
    user.setUsername(dto.getUsername());
    user.setPassword(dto.getPassword());
    return user;
  }
}
