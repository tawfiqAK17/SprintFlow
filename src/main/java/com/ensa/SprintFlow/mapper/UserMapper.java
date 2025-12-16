package com.ensa.SprintFlow.mapper;

import org.springframework.stereotype.Component;

import com.ensa.SprintFlow.dto.response.UserMetaDataResponseDto;
import com.ensa.SprintFlow.model.User;

@Component
public class UserMapper {
  public UserMetaDataResponseDto mapToMetaDataDto(User user) {
    UserMetaDataResponseDto dto = new UserMetaDataResponseDto();
    dto.setId(user.getId());
    dto.setEmail(user.getEmail());
    dto.setLastName(user.getLastName());
    dto.setFirstName(user.getFirstName());
    return dto;
  }
}
