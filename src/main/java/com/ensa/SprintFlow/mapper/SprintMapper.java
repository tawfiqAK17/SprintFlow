package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.sprint.request.SprintRequestDto;
import com.ensa.SprintFlow.dto.sprint.response.SprintMetaDataResponseDto;
import com.ensa.SprintFlow.dto.sprint.response.SprintResponseDto;
import com.ensa.SprintFlow.model.Sprint;
import org.springframework.stereotype.Component;

@Component
public class SprintMapper {

  public SprintMetaDataResponseDto mapToSprintMetaDadaResponseDto(Sprint sprint) {
    if (sprint == null) {
      return null;
    }
    return SprintMetaDataResponseDto.builder().id(sprint.getId()).title(sprint.getTitle()).build();
  }

  public Sprint mapToSprint(SprintRequestDto dto) {
    if (dto == null) {
      return null;
    }
    return Sprint.builder()
        .title(dto.getTitle())
        .startDate(dto.getStartDate())
        .endDate(dto.getEndDate())
        .build();
  }

  public SprintResponseDto maptoSprintResponseDto(Sprint sprint) {
    return SprintResponseDto.builder()
        .id(sprint.getId())
        .title(sprint.getTitle())
        .startDate(sprint.getStartDate())
        .endDate(sprint.getEndDate())
        .userStoriesCount(sprint.getUserStories() != null ? sprint.getUserStories().size() : 0)
        .build();
  }
}
