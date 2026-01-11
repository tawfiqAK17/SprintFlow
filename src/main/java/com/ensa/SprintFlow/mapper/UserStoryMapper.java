package com.ensa.SprintFlow.mapper;


import com.ensa.SprintFlow.dto.userStory.request.UserStoryRequestDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryMetaDataResponseDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryResponseDto;
import org.springframework.stereotype.Component;

import com.ensa.SprintFlow.model.UserStory;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserStoryMapper {
  EpicMapper epicMapper;
  SprintMapper sprintMapper;
  UserStoryDescriptionMapper userStoryDescriptionMapper;
  AcceptanceCriteriaMapper acceptanceCriteriaMapper;

  public UserStoryResponseDto mapToUserStoryResponseDto(UserStory userStory) {
    if (userStory == null){
      return null;
    }
    return UserStoryResponseDto.builder()
            .id(userStory.getId())
            .title(userStory.getTitle())
            .priority(userStory.getPriority())
            .epic( epicMapper.mapToEpicMetaDataResponseDto( userStory.getEpic()))
            .sprint(sprintMapper.mapToSprintMetaDadaResponseDto( userStory.getSprint()))
            .description( userStoryDescriptionMapper.mapToUserStoryDescriptionDto( userStory.getUserStoryDescription()))
            .acceptanceCriteria( acceptanceCriteriaMapper.mapToAcceptanceCriteriaDto( userStory.getAcceptanceCriteria()))
            .build();
  }

  public UserStoryMetaDataResponseDto mapToUserStoryMetaDataResponseDto( UserStory userStory){
    return UserStoryMetaDataResponseDto.builder()
            .id(userStory.getId())
            .title( userStory.getTitle())
            .build();
  }

  public UserStory mapToUserStory(UserStoryRequestDto dto){
    return UserStory.builder()
            .title( dto.getTitle())
            .priority( dto.getPriority())
            .userStoryDescription( userStoryDescriptionMapper.mapToUserStoryDescription(dto.getDescription()))
            .build();
  }

}
