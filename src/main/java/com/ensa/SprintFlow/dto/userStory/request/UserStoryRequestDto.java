package com.ensa.SprintFlow.dto.userStory.request;

import com.ensa.SprintFlow.dto.userStoryDescription.UserStoryDescriptionDto;

import com.ensa.SprintFlow.model.UserStoryMetrics;
import com.ensa.SprintFlow.util.FibonacciSequenceValidator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStoryRequestDto {
    @NotBlank(message = "title is required")
    private String title;
    @NotNull
    private UserStoryMetrics metrics;
    private UserStoryDescriptionDto description;
}