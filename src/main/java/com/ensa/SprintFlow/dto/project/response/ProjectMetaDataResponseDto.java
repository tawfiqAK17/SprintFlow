package com.ensa.SprintFlow.dto.project.response;

import com.ensa.SprintFlow.dto.user.response.UserMetaDataResponseDto;
import com.ensa.SprintFlow.enums.Role;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ProjectMetaDataResponseDto {

  private Long id;
  private String name;
  private String description;
  private LocalDateTime creationDate;
  private UserMetaDataResponseDto scrumMaster;
  private UserMetaDataResponseDto productOwner;
  private Role userRole;
}
