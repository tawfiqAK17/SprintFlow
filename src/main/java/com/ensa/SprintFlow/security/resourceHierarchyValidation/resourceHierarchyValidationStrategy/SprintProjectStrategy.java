package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy;

import com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators.SprintInProjectValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
public class SprintProjectStrategy extends ResourceHierarchyValidationStrategy {

  SprintInProjectValidator sprintInProjectValidator;

  public SprintProjectStrategy(
      AntPathMatcher pathMatcher, SprintInProjectValidator sprintInProjectValidator) {
    super(pathMatcher);
    this.sprintInProjectValidator = sprintInProjectValidator;
  }

  @Override
  protected String getUrlPattern() {
    return "/projects/{projectId}/sprints/{sprintId}";
  }

  @Override
  public void validate(String url) {
    Long projectId = Long.parseLong(getPathVariable(url, "projectId"));
    Long sprintId = Long.parseLong(getPathVariable(url, "sprintId"));
    sprintInProjectValidator.validate(projectId, sprintId);
  }
}
