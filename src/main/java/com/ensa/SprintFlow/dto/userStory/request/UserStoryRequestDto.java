package com.ensa.SprintFlow.dto.userStory.request;

import com.ensa.SprintFlow.dto.userStoryDescription.UserStoryDescriptionDto;

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
    private Integer priority;
    private UserStoryDescriptionDto description;
}
