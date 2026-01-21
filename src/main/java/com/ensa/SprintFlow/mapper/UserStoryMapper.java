package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.userStory.request.UserStoryRequestDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryMetaDataResponseDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryResponseDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryViewDto;
import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.model.UserStoryPriority;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class UserStoryMapper {
  EpicMapper epicMapper;
  SprintMapper sprintMapper;
  UserStoryDescriptionMapper userStoryDescriptionMapper;
  AcceptanceCriteriaMapper acceptanceCriteriaMapper;

  public UserStoryResponseDto mapToUserStoryResponseDto(UserStory userStory, UserStoryPriority priority) {
    if (userStory == null) {
      return null;
    }
    return UserStoryResponseDto.builder()
        .id(userStory.getId())
        .title(userStory.getTitle())
        .priority( priority)
            // metrics
        .epic(epicMapper.mapToEpicMetaDataResponseDto(userStory.getEpic()))
        .sprint(sprintMapper.mapToSprintMetaDadaResponseDto(userStory.getSprint()))
        .description(
                userStoryDescriptionMapper.mapToUserStoryDescriptionDto( userStory.getUserStoryDescription()))
        .acceptanceCriteria(
                acceptanceCriteriaMapper.mapToAcceptanceCriteriaDto( userStory.getAcceptanceCriteria()))
        .build();
  }

  public UserStoryMetaDataResponseDto mapToUserStoryMetaDataResponseDto(UserStory userStory) {
    return UserStoryMetaDataResponseDto.builder()
        .id(userStory.getId())
        .title(userStory.getTitle())
        .build();
  }

  public UserStoryViewDto mapToUserStoryViewDto(UserStory userStory, UserStoryPriority priority){
    return UserStoryViewDto.builder()
            .id( userStory.getId())
            .title( userStory.getTitle())
            .priority( priority)
            .epic( epicMapper.mapToEpicMetaDataResponseDto( userStory.getEpic()))
            .sprint( sprintMapper.mapToSprintMetaDadaResponseDto( userStory.getSprint()))
            .build();
  }

  public List<UserStoryViewDto> mapToUserStoryViewDto( List<UserStory> userStories, List<UserStoryPriority> priorities){
    List<UserStoryViewDto> userStoryViewDtoList = new ArrayList<>();
    int i = 0;
    for( UserStory userStory : userStories){
      userStoryViewDtoList.add( mapToUserStoryViewDto(userStory, priorities.get( i)));
      i++;
    }
    return userStoryViewDtoList;
  }

  public UserStory mapToUserStory(UserStoryRequestDto dto) {
    return UserStory.builder()
        .title(dto.getTitle())
        .metrics( dto.getMetrics())
        .userStoryDescription(
            userStoryDescriptionMapper.mapToUserStoryDescription(dto.getDescription()))
        .build();
  }
}
