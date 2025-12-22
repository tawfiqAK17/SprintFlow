package com.ensa.SprintFlow.config;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.exception.generalException.UnauthorizedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationManager {
  private AntPathMatcher pathMatcher;
  private HashMap<String, UrlPermition> urlsPermitions = new HashMap<>();

  class UrlPermition {
    private AuthorizationManager authorizationManager;
    private HashMap<Role, List<HttpMethod>> permitions = new HashMap<>();

    public UrlPermition(AuthorizationManager authorizationManager) {
      this.authorizationManager = authorizationManager;
    }

    public UrlPermition allow(Role role) {
      permitions.put(role, getAllMethods());
      return this;
    }

    public UrlPermition allowMethods(Role role, HttpMethod... httpMethods) {
      if (permitions.containsKey(role)) {
        permitions.get(role).addAll(List.of(httpMethods));
      } else {
        permitions.put(role, List.of(httpMethods));
      }
      return this;
    }

    public AuthorizationManager and() {
      return authorizationManager;
    }

    public AuthorizationManager done() {
      return authorizationManager;
    }

    public boolean isAuthorized(Role role, HttpMethod httpMethod) {
      if (!permitions.containsKey(role)) {
        return false;
      }
      return permitions.get(role).contains(httpMethod);
    }

    private List<HttpMethod> getAllMethods() {
      List<HttpMethod> httpMethods = new ArrayList<>();
      httpMethods.add(HttpMethod.GET);
      httpMethods.add(HttpMethod.POST);
      httpMethods.add(HttpMethod.PUT);
      httpMethods.add(HttpMethod.DELETE);
      return httpMethods;
    }
  }

  public UrlPermition forUrl(String urlPattern) {
    UrlPermition urlPermition = new UrlPermition(this);
    urlsPermitions.put(urlPattern, urlPermition);
    return urlPermition;
  }

  public boolean isAuthorized(String url, Role role, HttpMethod httpMethod) {
    String urlPattern =
        urlsPermitions.keySet().stream()
            .filter(pattern -> pathMatcher.match(pattern, url))
            .findFirst()
            .orElseThrow(() -> new UnauthorizedException("the given url is not valid"));

    return urlsPermitions.get(urlPattern).isAuthorized(role, httpMethod);
  }
}
