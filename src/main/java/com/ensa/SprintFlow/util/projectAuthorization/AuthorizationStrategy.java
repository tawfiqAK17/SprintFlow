package com.ensa.SprintFlow.util.projectAuthorization;

import org.springframework.http.HttpMethod;

import com.ensa.SprintFlow.enums.Role;

public interface AuthorizationStrategy {
  public boolean isAuthorized(String url, HttpMethod method);
  public boolean supports(Role role);
}
