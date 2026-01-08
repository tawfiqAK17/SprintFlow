package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy;

import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.service.ProjectService;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

@Component
public class ValidateSprintInProjectStrategy extends ResourceHierarchyValidationStrategy<Long, Long> {

  private ProjectService projectService;

  public ValidateSprintInProjectStrategy(
      AntPathMatcher pathMatcher, ProjectService projectService) {
    super(pathMatcher);
    this.projectService = projectService;
  }

  @Override
  protected String getUrlPattern() {
    return "/projects/{projectId}/sprints/{sprintId}";
  }

  @Override
  public void validate(String url) {
    Long projectId = Long.parseLong(getPathVariable(url, "projectId"));
    Long sprintId = Long.parseLong(getPathVariable(url, "sprintId"));
    validate(projectId, sprintId);
  }

  @Override
  public void validate(Long parent, Long child) {
    if (!projectService.findById(parent).getSprints().stream().anyMatch(s -> s.getId() == child)) {
      throw new NotFoundException("the project does not have any sprint with the given id");
    }
  }
}
