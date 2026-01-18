package com.ensa.SprintFlow.security.resourceHierarchyValidation.resourceHierarchyValidationStrategy.validators;

import org.springframework.stereotype.Component;

import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.service.EpicService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserStoryInEpicValidator {
  EpicService epicService;
  public void validate(Long epicId, Long userStoryId) {
    if (!epicService.findById(epicId).getUserStories().stream().anyMatch(u -> u.getId() == userStoryId)) {
      throw new NotFoundException("the epic has no user story with the given id");
    }
  }
}
