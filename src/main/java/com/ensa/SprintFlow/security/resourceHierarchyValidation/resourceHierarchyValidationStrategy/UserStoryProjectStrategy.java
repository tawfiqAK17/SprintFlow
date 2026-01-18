package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy;

import com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators.UserStoryInProjectValidator;
import org.springframework.util.AntPathMatcher;

public class UserStoryProjectStrategy extends ResourceHierarchyValidationStrategy {

  UserStoryInProjectValidator userStoryInProjectValidator;

  public UserStoryProjectStrategy(
      AntPathMatcher pathMatcher, UserStoryInProjectValidator userStoryInProjectValidator) {
    super(pathMatcher);
    this.userStoryInProjectValidator = userStoryInProjectValidator;
  }

  @Override
  protected String getUrlPattern() {
    return "/projects/{projectId}/user_stories/{userStroyId}";
  }

  @Override
  public void validate(String url) {
    Long projectId = Long.parseLong(getPathVariable(url, "projectId"));
    Long userStoryId = Long.parseLong(getPathVariable(url, "userStoryId"));
    userStoryInProjectValidator.validate(projectId, userStoryId);
  }
}
