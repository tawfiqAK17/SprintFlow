package com.ensa.SprintFlow.dto.task.response;

import com.ensa.SprintFlow.dto.userStory.response.UserStoryMetaDataResponseDto;
import com.ensa.SprintFlow.enums.TaskStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class TaskMetaDataResponseDto {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private UserStoryMetaDataResponseDto userStory;
}
