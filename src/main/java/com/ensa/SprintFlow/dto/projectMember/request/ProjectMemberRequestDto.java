package com.ensa.SprintFlow.dto.projectMember.request;

import com.ensa.SprintFlow.enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberRequestDto {
  @NotBlank(message = "username is required")
  private String username;

  @NotBlank(message = "Role is required")
  private Role role;
}
