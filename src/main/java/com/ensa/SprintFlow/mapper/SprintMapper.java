package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.response.SprintMetaDateResponseDto;
import com.ensa.SprintFlow.model.Sprint;

public class SprintMapper {

  public SprintMetaDateResponseDto mapToSprintMetaDadaResponseDto(Sprint sprint) {
    return SprintMetaDateResponseDto.builder()
        .id(sprint.getId())
        .title(sprint.getTitle())
        .startDate(sprint.getStartDate())
        .endDate(sprint.getEndDate())
        .build();
  }
}
