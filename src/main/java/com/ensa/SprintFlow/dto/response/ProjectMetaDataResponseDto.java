package com.ensa.SprintFlow.dto.response;

import com.ensa.SprintFlow.enums.Role;
import java.time.LocalDateTime;

public class ProjectMetaDataResponseDto {

  private Long id;
  private String name;
  private String description;
  private LocalDateTime creationDate;
  private UserMetaDataResponseDto scrumMaster;
  private UserMetaDataResponseDto productOwner;
  private Role userRole;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

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

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public Role getUserRole() {
    return userRole;
  }

  public void setUserRole(Role userRole) {
    this.userRole = userRole;
  }

  public UserMetaDataResponseDto getScrumMaster() {
    return scrumMaster;
  }

  public void setScrumMaster(UserMetaDataResponseDto scrumMaster) {
    this.scrumMaster = scrumMaster;
  }

  public UserMetaDataResponseDto getProductOwner() {
    return productOwner;
  }

  public void setProductOwner(UserMetaDataResponseDto productOwner) {
    this.productOwner = productOwner;
  }
}
