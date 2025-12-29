package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.response.UserStoryResponseDto;
import com.ensa.SprintFlow.model.UserStory;

public class UserStoryMapper {

  SprintMapper sprintMapper;

  public UserStoryResponseDto mapToUserStoryResponseDto(UserStory userStory) {
    return UserStoryResponseDto.builder()
        .id(userStory.getId())
        .title(userStory.getTitle())
        .priority(userStory.getPriority())
        .sprint(sprintMapper.mapToSprintMetaDadaResponseDto(userStory.getSprint()))
        .build();
  }
}
