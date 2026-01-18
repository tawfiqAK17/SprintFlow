package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators;

import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserStoryInProjectValidator {
  ProjectService projectService;

  public void validate(Long parent, Long child) {
    if (!projectService.findById(parent).getDefaultEpic().getUserStories().stream()
        .anyMatch(u -> u.getId() == child)) {
      throw new NotFoundException("the project has no user story with the given id");
    }
  }
}
