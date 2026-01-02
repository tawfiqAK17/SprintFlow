package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy;

import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.service.ProjectService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
public class ValidateEpicInProjectStrategy extends ResourceHierarchyValidationStrategy {

  ProjectService projectService;

  public ValidateEpicInProjectStrategy(AntPathMatcher pathMatcher, ProjectService projectService) {
    super(pathMatcher);
    this.projectService = projectService;
  }

  @Override
  protected String getUrlPattern() {
    return "/projects/{projectId}/epics/{epicId}";
  }

  @Override
  public void validate(String url) {
    Long parent = Long.parseLong(getPathVariable(url, "projectId"));
    Long child = Long.parseLong(getPathVariable(url, "epicId"));
    validate(parent, child);
  }

  @Override
  public void validate(Long parent, Long child) {
    if (!projectService.findById(parent).getEpics().stream().anyMatch(e -> e.getId() == child)) {
      throw new NotFoundException("the project does not have an epic with the given id");
    }
  }
}
