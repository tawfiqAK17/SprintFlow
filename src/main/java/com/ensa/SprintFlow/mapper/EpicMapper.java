package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.epic.request.EpicRequestDto;
import com.ensa.SprintFlow.dto.epic.response.EpicMetaDataResponseDto;
import com.ensa.SprintFlow.dto.epic.response.EpicResponseDto;
import com.ensa.SprintFlow.model.Epic;
import com.ensa.SprintFlow.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EpicMapper {

  @Mapping(target = "project", source = "project")
  @Mapping(target = "title", source = "dto.title")
  @Mapping(target = "description", source = "dto.description")
  @Mapping(target = "userStories", ignore = true)
  Epic mapToEpic(Project project, EpicRequestDto dto);

  EpicMetaDataResponseDto mapToEpicMetaDataResponseDto(Epic epic);

  @Mapping(
      target = "userStoriesCount",
      expression = "java(epic.getUserStories() != null ? epic.getUserStories().size() : 0)")
  EpicResponseDto mapToEpicResponseDto(Epic epic);
}
