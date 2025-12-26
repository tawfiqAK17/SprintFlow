package com.ensa.SprintFlow.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class ProjectRequestDto {

  @NotBlank(message = "name is required")
  private String name;

  @NotBlank(message = "description is required")
  private String description;

  @NotBlank(message = "scrum master username is required")
  private String scrumMasterUsername;
}
