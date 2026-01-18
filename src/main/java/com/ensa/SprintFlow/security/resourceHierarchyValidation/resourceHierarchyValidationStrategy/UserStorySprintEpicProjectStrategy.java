package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy;

import com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators.EpicInProjectValidator;
import com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators.SprintInProjectValidator;
import com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators.UserStoryInEpicValidator;
import com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators.UserStoryInSprintValidator;
import java.util.Set;
import org.springframework.util.AntPathMatcher;

public class UserStorySprintEpicProjectStrategy extends ResourceHierarchyValidationStrategy {

  EpicInProjectValidator epicInProjectValidator;
  SprintInProjectValidator sprintInProjectValidator;
  UserStoryInEpicValidator userStoryInEpicValidator;
  UserStoryInSprintValidator userStoryInSprintValidator;

  public UserStorySprintEpicProjectStrategy(
      AntPathMatcher pathMatcher,
      EpicInProjectValidator epicInProjectValidator,
      SprintInProjectValidator sprintInProjectValidator,
      UserStoryInEpicValidator userStoryInEpicValidator,
      UserStoryInSprintValidator userStoryInSprintValidator) {
    super(pathMatcher);
    this.epicInProjectValidator = epicInProjectValidator;
    this.sprintInProjectValidator = sprintInProjectValidator;
    this.userStoryInSprintValidator = userStoryInSprintValidator;
    this.userStoryInEpicValidator = userStoryInEpicValidator;
  }

  @Override
  protected String getUrlPattern() {
    return "/projects/{projectId}/user_stories/{userStroyId}";
  }

  @Override
  protected Set<String> getQueryParams() {
    return Set.of("epic_id", "sprint_id");
  }

  @Override
  public void validate(String url) {
    Long projectId = Long.parseLong(getPathVariable(url, "projectId"));
    Long userStoryId = Long.parseLong(getPathVariable(url, "userStoryId"));
    Long epicId = Long.parseLong(getQueryParam(url, "epic_id"));
    Long sprintId = Long.parseLong(getQueryParam(url, "sprint_id"));
    epicInProjectValidator.validate(projectId, epicId);
    sprintInProjectValidator.validate(projectId, sprintId);
    userStoryInEpicValidator.validate(epicId, userStoryId);
    userStoryInSprintValidator.validate(sprintId, userStoryId);
  }
}
