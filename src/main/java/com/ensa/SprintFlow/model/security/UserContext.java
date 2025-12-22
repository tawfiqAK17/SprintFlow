package com.ensa.SprintFlow.model.security;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.model.User;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserContext {
  private Long id;

  private String username;
  private String email;
  // roles will hold the user roles in a project
  private List<Role> roles;

  public UserContext(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.email = user.getEmail();
    this.roles = new ArrayList<>();
  }
}
