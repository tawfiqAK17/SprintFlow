package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.user.response.UserMetaDataResponseDto;
import com.ensa.SprintFlow.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
  public UserMetaDataResponseDto mapToMetaDataDto(User user) {
    if (user == null) {
      return null;
    }
    return UserMetaDataResponseDto.builder()
        .id(user.getId())
        .email(user.getEmail())
        .username(user.getUsername())
        .lastName(user.getLastName())
        .firstName(user.getFirstName())
        .build();
  }
}
