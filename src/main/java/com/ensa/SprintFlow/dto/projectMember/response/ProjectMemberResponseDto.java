package com.ensa.SprintFlow.dto.projectMember.response;

import com.ensa.SprintFlow.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProjectMemberResponseDto {
  private String username;
  private String firstName;
  private String lastName;
  private String email;
  private Role role;
}
