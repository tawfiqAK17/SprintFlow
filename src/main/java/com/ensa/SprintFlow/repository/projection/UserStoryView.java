package com.ensa.SprintFlow.repository.projection;

import com.ensa.SprintFlow.dto.epic.response.EpicMetaDataResponseDto;
import com.ensa.SprintFlow.dto.sprint.response.SprintMetaDataResponseDto;

public record UserStoryView(Long id, String title, Integer priority, EpicMetaDataResponseDto epic, SprintMetaDataResponseDto sprint) {

    public UserStoryView(Long id, String title, Integer priority, Long epicId,
                         String epicTitle, Long sprintId, String sprintTitle) {
        this(
            id,
            title,
            priority,
            (epicId == null) ? null : new EpicMetaDataResponseDto(epicId, epicTitle),
            (sprintId == null) ? null : new SprintMetaDataResponseDto(sprintId, sprintTitle)
        );
    }
}
