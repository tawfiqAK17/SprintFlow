package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy;

import com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators.SprintInProjectValidator;
import com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators.UserStoryInSprintValidator;
import org.springframework.util.AntPathMatcher;

public class UserStorySprintProjectStrategy extends ResourceHierarchyValidationStrategy {

  UserStoryInSprintValidator userStoryInSprintValidator;
  SprintInProjectValidator sprintInProjectValidator;

  public UserStorySprintProjectStrategy(
      AntPathMatcher pathMatcher,
      UserStoryInSprintValidator userStoryInSprintValidator,
      SprintInProjectValidator sprintInProjectValidator) {
    super(pathMatcher);
    this.userStoryInSprintValidator = userStoryInSprintValidator;
    this.sprintInProjectValidator = sprintInProjectValidator;
  }

  @Override
  protected String getUrlPattern() {
    return "/projects/{projectId}/sprints/{sprintId}/user_stories/{userStoryId}";
  }

  @Override
  public void validate(String url) {
    Long projectId = Long.parseLong(getPathVariable(url, "projectId"));
    Long sprintId = Long.parseLong(getPathVariable(url, "sprintId"));
    Long userStoryId = Long.parseLong(getPathVariable(url, "userStoryId"));
    sprintInProjectValidator.validate(projectId, sprintId);
    userStoryInSprintValidator.validate(sprintId, userStoryId);
  }
}
