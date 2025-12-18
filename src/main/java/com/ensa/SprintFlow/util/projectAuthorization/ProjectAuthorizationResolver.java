package com.ensa.SprintFlow.util.projectAuthorization;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import java.util.List;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
public class ProjectAuthorizationResolver {

  private AntPathMatcher pathMatcher = new AntPathMatcher();
  List<AuthorizationStrategy> authorizationStrategies;

  ProjectAuthorizationResolver(List<AuthorizationStrategy> authorizationStrategies) {
    this.authorizationStrategies = authorizationStrategies;
  }

  public boolean isAuthorized(HttpMethod method, String url, List<Role> roles) {
    for (Role role : roles) {
      AuthorizationStrategy strategy =
          authorizationStrategies.stream()
              .filter(s -> s.supports(role))
              .findFirst()
              .orElseThrow(() -> new NotFoundException("there is no role named" + role));
      return strategy.isAuthorized(url, method);
    }
    return false;
  }
}
