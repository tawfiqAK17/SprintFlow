package com.ensa.SprintFlow.dto.user.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserMetaDataResponseDto {
  private Long id;
  private String firstName;
  private String lastName;
  private String username;
  private String email;
}
