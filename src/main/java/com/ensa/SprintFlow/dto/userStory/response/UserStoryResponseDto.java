package com.ensa.SprintFlow.dto.userStory.response;


import com.ensa.SprintFlow.dto.acceptanceCriteria.AcceptanceCriteriaDto;
import com.ensa.SprintFlow.dto.epic.response.EpicMetaDataResponseDto;
import com.ensa.SprintFlow.dto.sprint.response.SprintMetaDataResponseDto;
import com.ensa.SprintFlow.dto.userStoryDescription.UserStoryDescriptionDto;
import com.ensa.SprintFlow.model.UserStoryMetrics;
import com.ensa.SprintFlow.model.UserStoryPriority;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class UserStoryResponseDto {
    private Long id;
    private String title;
    private UserStoryMetrics metrics;
    private UserStoryPriority priority;
    private EpicMetaDataResponseDto epic;
    private SprintMetaDataResponseDto sprint;
    private UserStoryDescriptionDto description;
    private List<AcceptanceCriteriaDto> acceptanceCriteria;
}
