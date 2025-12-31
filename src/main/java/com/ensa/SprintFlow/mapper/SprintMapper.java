package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.sprint.response.SprintMetaDataResponseDto;
import com.ensa.SprintFlow.model.Sprint;
import com.ensa.SprintFlow.repository.projection.SprintView;
import org.springframework.stereotype.Component;

@Component
public class SprintMapper {

  public SprintMetaDataResponseDto mapToSprintMetaDadaResponseDto(SprintView sprint) {
    if (sprint == null){
      return null;
    }
    return SprintMetaDataResponseDto.builder()
        .id(sprint.id())
        .title(sprint.title())
        .build();
  }

  public SprintMetaDataResponseDto mapToSprintMetaDadaResponseDto(Sprint sprint) {
    if (sprint == null){
      return null;
    }
    return SprintMetaDataResponseDto.builder()
            .id(sprint.getId())
            .title(sprint.getTitle())
            .build();
  }
}
