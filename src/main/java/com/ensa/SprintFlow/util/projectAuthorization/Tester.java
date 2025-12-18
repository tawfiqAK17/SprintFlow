package com.ensa.SprintFlow.util.projectAuthorization;

import com.ensa.SprintFlow.enums.Role;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class Tester implements AuthorizationStrategy {

  @Override
  public boolean isAuthorized(String url, HttpMethod method) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'isAuthorized'");
  }

  @Override
  public boolean supports(Role role) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'supports'");
  }
}
