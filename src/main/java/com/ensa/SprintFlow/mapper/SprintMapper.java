package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.sprint.request.SprintRequestDto;
import com.ensa.SprintFlow.dto.sprint.response.SprintMetaDataResponseDto;
import com.ensa.SprintFlow.dto.sprint.response.SprintResponseDto;
import com.ensa.SprintFlow.model.Sprint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SprintMapper {

    SprintMetaDataResponseDto mapToSprintMetaDadaResponseDto(Sprint sprint);

    Sprint mapToSprint(SprintRequestDto dto);

    @Mapping(target = "userStoriesCount", expression = "java(sprint.getUserStories() != null ? sprint.getUserStories().size() : 0)")
    SprintResponseDto maptoSprintResponseDto(Sprint sprint);
}
