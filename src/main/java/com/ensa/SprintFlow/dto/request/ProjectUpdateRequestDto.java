package com.ensa.SprintFlow.dto.request;


public class ProjectUpdateRequestDto {

  private String name;

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
