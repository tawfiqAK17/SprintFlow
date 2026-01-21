package com.ensa.SprintFlow.security.model;

import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserContext {
  private Long id;

  private String username;
  private String email;
  // the project the user trying to access
  private Project project;

  public UserContext(User user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.email = user.getEmail();
  }
}
