package com.ensa.SprintFlow.util;

import com.ensa.SprintFlow.config.AuthorizationManager;
import com.ensa.SprintFlow.enums.Role;
import java.util.List;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class ProjectAuthorizationResolver {

  AuthorizationManager authorizationManager;

  ProjectAuthorizationResolver(AuthorizationManager authorizationManager) {
    this.authorizationManager = authorizationManager;
  }

  public boolean isAuthorized(HttpMethod method, String url, List<Role> roles) {
    for (Role role : roles) {
      if (authorizationManager.isAuthorized(url, role, method)) {
        return true;
      }
    }
    return false;
  }
}
