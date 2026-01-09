package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators;

import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EpicInProjectValidator {
  ProjectService projectService;

  public void validate(Long parent, Long child) {
    if (!projectService.findById(parent).getEpics().stream().anyMatch(e -> e.getId() == child)) {
      throw new NotFoundException("the project does not have an epic with the given id");
    }
  }
}
