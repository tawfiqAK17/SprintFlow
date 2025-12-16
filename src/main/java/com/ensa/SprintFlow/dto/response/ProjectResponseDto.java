package com.ensa.SprintFlow.dto.response;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ProjectResponseDto {
  private Long id;
  private String name;
  private String description;
  private LocalDateTime creationDate;
  private UserResponseDto scrumMaster;
  private UserResponseDto productOwner;
  private List<Map<User, Role>> members;
  private List<Map<String, String>> statistics;

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

  public UserResponseDto getScrumMaster() {
    return scrumMaster;
  }

  public void setScrumMaster(UserResponseDto scrumMaster) {
    this.scrumMaster = scrumMaster;
  }

  public UserResponseDto getProductOwner() {
    return productOwner;
  }

  public void setProductOwner(UserResponseDto productOwner) {
    this.productOwner = productOwner;
  }

  public List<Map<User, Role>> getMembers() {
    return members;
  }

  public void setMembers(List<Map<User, Role>> members) {
    this.members = members;
  }

  public List<Map<String, String>> getStatistics() {
    return statistics;
  }

  public void setStatistics(List<Map<String, String>> statistics) {
    this.statistics = statistics;
  }
}
