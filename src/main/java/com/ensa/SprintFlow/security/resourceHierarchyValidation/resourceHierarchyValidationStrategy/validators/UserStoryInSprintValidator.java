package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators;

import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.service.SprintService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserStoryInSprintValidator {
  SprintService sprintService;

  public void validate(Long parent, Long child) {
    if (!sprintService.findById(parent).getUserStories().stream()
        .anyMatch(u -> u.getId() == child)) {
      throw new NotFoundException("the sprint does not have any user story with the given id");
    }
  }
}
