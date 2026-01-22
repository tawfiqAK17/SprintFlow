package com.ensa.SprintFlow.dto.userStory.request;

import com.ensa.SprintFlow.dto.userStoryDescription.UserStoryDescriptionDto;

import com.ensa.SprintFlow.model.UserStoryMetrics;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStoryRequestDto {
    @NotBlank(message = "title is required")
    private String title;
    private UserStoryMetrics metrics;  // we should validate metrics look at the method below
    private UserStoryDescriptionDto description;
}


/*
 private void validateUserStoryMetrics(UserStoryMetrics metrics){
    if ( !FibonacciSequenceValidator.isValid(
            metrics.getBusinessValue(),
            metrics.getEffort(),
            metrics.getUrgency(),
            metrics.getRiskReduction()
      )){
      return; // throw an exception
    }
  }
*/