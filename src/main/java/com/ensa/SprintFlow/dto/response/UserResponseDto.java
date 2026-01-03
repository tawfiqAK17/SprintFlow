package com.ensa.SprintFlow.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.ensa.SprintFlow.dto.project.response.ProjectMetaDataResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private LocalDateTime enrollDate;
  private List<ProjectMetaDataResponseDto> projects;
}
