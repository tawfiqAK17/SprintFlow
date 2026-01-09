package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy;

import com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators.EpicInProjectValidator;
import com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators.UserStoryInEpicValidator;
import java.util.Set;
import org.springframework.util.AntPathMatcher;

public class UserStoryEpicProjectStrategy extends ResourceHierarchyValidationStrategy {

  EpicInProjectValidator epicInProjectValidator;
  UserStoryInEpicValidator userStoryInEpicValidator;

  public UserStoryEpicProjectStrategy(
      AntPathMatcher pathMatcher,
      EpicInProjectValidator epicInProjectValidator,
      UserStoryInEpicValidator userStoryInEpicValidator) {
    super(pathMatcher);
    this.epicInProjectValidator = epicInProjectValidator;
    this.userStoryInEpicValidator = userStoryInEpicValidator;
  }

  @Override
  protected String getUrlPattern() {
    return "/projects/{projectId}/user_stories/{userStoryId}";
  }

  @Override
  protected Set<String> getQueryParams() {
    return Set.of("epic_id");
  }

  @Override
  public void validate(String url) {
    Long projectId = Long.parseLong(getPathVariable(url, "projectId"));
    Long userStoryId = Long.parseLong(getPathVariable(url, "userStory"));
    Long epicId = Long.parseLong(getQueryParam(url, "epic_id"));
    epicInProjectValidator.validate(projectId, epicId);
    userStoryInEpicValidator.validate(epicId, userStoryId);
  }
}
