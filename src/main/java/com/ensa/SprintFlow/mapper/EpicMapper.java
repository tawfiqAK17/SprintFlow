package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.request.EpicCreationRequestDto;
import com.ensa.SprintFlow.dto.epic.response.EpicMetaDataResponseDto;
import com.ensa.SprintFlow.dto.response.EpicResponseDto;
import com.ensa.SprintFlow.model.Epic;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.repository.projection.EpicView;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EpicMapper {

  UserStoryMapper userStoryMapper;

  public Epic mapToEpic(Project project, EpicCreationRequestDto dto) {
    return Epic.builder()
        .title(dto.getTitle())
        .description(dto.getDescription())
        .project(project)
        .build();
  }

//  public EpicMetaDataResponseDto mapToEpicMetaDataResponseDto(Epic epic) {
//    return EpicMetaDataResponseDto.builder()
//        .description(epic.getDescription())
//        .title(epic.getTitle())
//        .id(epic.getId())
//        .userStoriesCount(0L) // TODO should be counted
//        .build();
//  }

  // Abdessalam implementation
  public EpicMetaDataResponseDto mapToEpicMetaDataResponseDto(Epic epic) {
    if (epic == null){
      return null;
    }
    return EpicMetaDataResponseDto.builder()
            .title(epic.getTitle())
            .id(epic.getId()).build();
  }

  public EpicMetaDataResponseDto mapToEpicMetaDataResponseDto(EpicView epic) {
    if (epic == null){
      return null;
    }
    return EpicMetaDataResponseDto.builder()
          .title(epic.title())
          .id(epic.id()).build();
    }

  // There is a problem here due of two UserStoryMetaData implementation
  /*
  public EpicResponseDto mapToEpicResponseDto(Epic epic) {
    return EpicResponseDto.builder()
        .title(epic.getTitle())
        .description(epic.getDescription())
        .id(epic.getId())
        .userStories(
            epic.getUserStories().stream()
                .map(u -> userStoryMapper.mapToUserStoryResponseDto(u))
                .toList())
        .build();
  }
   */
}
