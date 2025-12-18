package com.ensa.SprintFlow.util.projectAuthorization;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.ensa.SprintFlow.enums.Role;

@Component
public class Developer implements AuthorizationStrategy {

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
