package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.UserDto;
import com.ensa.SprintFlow.model.User;

public class UserMapper implements Mapper<User, UserDto> {

  @Override
  public UserDto mapToDto(User entity) {
    UserDto dto = new UserDto();
    dto.setFirstName(entity.getFirstName());
    dto.setLastName(entity.getLastName());
    return dto;
  }

  @Override
  public User mapToEntity(UserDto dto) {
    User user = new User();
    user.setFirstName(dto.getFirstName());
    user.setLastName(dto.getLastName());
    return user;
  }
}
