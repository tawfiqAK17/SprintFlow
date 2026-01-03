package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.epic.request.EpicRequestDto;
import com.ensa.SprintFlow.dto.epic.response.EpicMetaDataResponseDto;
import com.ensa.SprintFlow.dto.epic.response.EpicResponseDto;
import com.ensa.SprintFlow.model.Epic;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.repository.projection.EpicView;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EpicMapper {

  public Epic mapToEpic(Project project, EpicRequestDto dto) {

    return Epic.builder()
        .title(dto.getTitle())
        .description(dto.getDescription())
        .project(project)
        .build();
  }

public EpicResponseDto mapToEpicResponseDto(Epic epic) {
    return EpicResponseDto.builder()
        .description(epic.getDescription())
        .title(epic.getTitle())
        .id(epic.getId())
        .userStoriesCount(0L) // TODO should be counted
        .build();
  }

}
