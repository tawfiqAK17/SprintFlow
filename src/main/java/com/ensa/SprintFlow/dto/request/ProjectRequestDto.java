package com.ensa.SprintFlow.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ProjectRequestDto {

  @NotBlank(message = "name is required")
  private String name;

  @NotBlank(message = "description is required")
  private String description;

  private String scrumMasterUsername;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getScrumMasterUsername() {
    return scrumMasterUsername;
  }

  public void setScrumMasterUsername(String scrumMasterUsername) {
    this.scrumMasterUsername = scrumMasterUsername;
  }
}
