package com.ensa.SprintFlow.dto.userStory.request;

import com.ensa.SprintFlow.dto.userStoryDescription.UserStoryDescriptionDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserStoryRequestDto {
    @NotBlank(message = "title is required")
    private String title;
    private Integer priority;
    private UserStoryDescriptionDto description;
}
