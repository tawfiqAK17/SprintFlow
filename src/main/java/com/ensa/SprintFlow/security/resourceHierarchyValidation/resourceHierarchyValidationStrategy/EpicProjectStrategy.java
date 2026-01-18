package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy;

import com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators.EpicInProjectValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
public class EpicProjectStrategy extends ResourceHierarchyValidationStrategy {

  EpicInProjectValidator epicInProjectValidator;

  public EpicProjectStrategy(
      AntPathMatcher pathMatcher, EpicInProjectValidator epicInProjectValidator) {
    super(pathMatcher);
    this.epicInProjectValidator = epicInProjectValidator;
  }

  @Override
  protected String getUrlPattern() {
    return "/projects/{projectId}/epics/{epicId}";
  }

  @Override
  public void validate(String url) {
    Long parent = Long.parseLong(getPathVariable(url, "projectId"));
    Long child = Long.parseLong(getPathVariable(url, "epicId"));
    epicInProjectValidator.validate(parent, child);
  }
}
