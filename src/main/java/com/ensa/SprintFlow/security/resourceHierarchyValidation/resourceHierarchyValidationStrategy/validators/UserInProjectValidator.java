package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators;

import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.service.ProjectMemberService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserInProjectValidator {
  ProjectMemberService projectMemberService;

  public void validate(Long parent, String child) {
    projectMemberService.findAllByProjectIdAndUsername(parent, child).stream()
        .findAny()
        .orElseThrow(
            () -> new NotFoundException("the project has no member with the given username"));
  }
}
