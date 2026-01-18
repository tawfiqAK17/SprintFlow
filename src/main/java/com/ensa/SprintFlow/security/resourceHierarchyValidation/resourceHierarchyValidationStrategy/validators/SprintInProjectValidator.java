package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators;

import org.springframework.stereotype.Component;

import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.service.ProjectService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class SprintInProjectValidator {
  ProjectService projectService;
  public void validate(Long parent, Long child) {
    if (!projectService.findById(parent).getSprints().stream().anyMatch(s -> s.getId() == child)) {
      throw new NotFoundException("the project does not have any sprint with the given id");
    }
  }
}
