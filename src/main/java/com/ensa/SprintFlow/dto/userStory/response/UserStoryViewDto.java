package com.ensa.SprintFlow.dto.userStory.response;

import com.ensa.SprintFlow.dto.epic.response.EpicMetaDataResponseDto;
import com.ensa.SprintFlow.dto.sprint.response.SprintMetaDataResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Builder
public class UserStoryViewDto {
    private Long id;
    private String title;
    private Integer priority;
    private EpicMetaDataResponseDto epic;
    private SprintMetaDataResponseDto sprint;
}
