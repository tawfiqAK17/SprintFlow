package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.request.EpicCreationRequestDto;
import com.ensa.SprintFlow.dto.response.EpicMetaDataResponseDto;
import com.ensa.SprintFlow.model.Epic;
import com.ensa.SprintFlow.model.Project;
import org.springframework.stereotype.Component;

@Component
public class EpicMapper {

  public Epic mapToEpic(Project project, EpicCreationRequestDto dto) {
    return Epic.builder()
        .title(dto.getTitle())
        .description(dto.getDescription())
        .project(project)
        .build();
  }

  public EpicMetaDataResponseDto mapToEpicMetaDataResponseDto(Epic epic) {
    return EpicMetaDataResponseDto.builder()
        .description(epic.getDescription())
        .title(epic.getTitle())
        .id(epic.getId())
        .userStoriesCount(0L) // TODO should be counted
        .build();
  }
}
