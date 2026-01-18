package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@AllArgsConstructor
public abstract class ResourceHierarchyValidationStrategy {
  protected AntPathMatcher pathMatcher;

  protected abstract String getUrlPattern();

  protected Set<String> getQueryParams() {
    return new HashSet<>();
  }

  public boolean supports(String url) {

    // get the url pattern and required query params for the strategy
    String urlPattern = getUrlPattern();
    Set<String> requiredQueryParams = getQueryParams();

    // check if the url provided match the strategy url pattern
    if (!pathMatcher.match(urlPattern, url)) {
      return false;
    }

    // extract the url query params
    Map<String, List<String>> queryParams =
        UriComponentsBuilder.fromUriString(url).build().getQueryParams();

    // check if all the required query params are exist in the url
    if (!queryParams.keySet().equals(requiredQueryParams)) {
      return false;
    }

    return true;
  }

  public abstract void validate(String url);

  protected String getPathVariable(String url, String variable) {
    Map<String, String> pathVariables =
        pathMatcher.extractUriTemplateVariables(getUrlPattern(), url);
    return pathVariables.get(variable);
  }

  protected String getQueryParam(String url, String param) {
    Map<String, List<String>> queryParams =
        UriComponentsBuilder.fromUriString(url).build().getQueryParams();
    // if the query is repeated multiple times get the first one
    return queryParams.get(param).getFirst();
  }
}
