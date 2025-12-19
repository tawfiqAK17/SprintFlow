package com.ensa.SprintFlow.dto.response;

import com.ensa.SprintFlow.enums.Role;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

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
