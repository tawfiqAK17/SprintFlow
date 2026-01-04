package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy;

import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.service.SprintService;
import org.springframework.util.AntPathMatcher;

public class ValidateUserStroryInSprintStrategy extends ResourceHierarchyValidationStrategy {

  SprintService sprintService;
  ValidateSprintInProjectStrategy validateSprintInProjectStrategy;

  public ValidateUserStroryInSprintStrategy(
      AntPathMatcher pathMatcher,
      SprintService sprintService,
      ValidateSprintInProjectStrategy validateSprintInProjectStrategy) {
    super(pathMatcher);
    this.sprintService = sprintService;
    this.validateSprintInProjectStrategy = validateSprintInProjectStrategy;
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
    validateSprintInProjectStrategy.validate(projectId, sprintId);
    validate(sprintId, userStoryId);
  }

  @Override
  public void validate(Long parent, Long child) {
    if (!sprintService.findById(parent).getUserStories().stream().anyMatch(u -> u.getId() == child)) {
      throw new NotFoundException("the sprint does not have any user story with the given id");
    }
  }
}
