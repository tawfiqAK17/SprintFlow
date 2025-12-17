package com.ensa.SprintFlow.model.security;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.model.User;
import java.util.ArrayList;
import java.util.List;

public class UserContext {
  private Long id;

  private String username;
  private String email;
  // roles will hold the user roles in a project
  private List<Role> roles;

  public UserContext(User user) {
    this.id = user.getId();
    this.username = user.getUserName();
    this.email = user.getEmail();
    this.roles = new ArrayList<>();
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<Role> getRoles() {
    return roles;
  }

  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
