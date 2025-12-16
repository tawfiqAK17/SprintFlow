package com.ensa.SprintFlow.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class UserResponseDto {
  private Long id;
  private String firstName;
  private String lastName;
  private String email;
  private LocalDateTime enrollDate;
  private List<ProjectMetaDataResponseDto> projects;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDateTime getEnrollDate() {
    return enrollDate;
  }

  public void setEnrollDate(LocalDateTime enrollDate) {
    this.enrollDate = enrollDate;
  }

  public List<ProjectMetaDataResponseDto> getProjects() {
    return projects;
  }

  public void setProjects(List<ProjectMetaDataResponseDto> projects) {
    this.projects = projects;
  }
}
