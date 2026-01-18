package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.dto.userStory.request.UserStoryRequestDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryResponseDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryViewDto;
import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.mapper.UserStoryMapper;
import com.ensa.SprintFlow.model.Epic;
import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.model.UserStoryDescription;
import com.ensa.SprintFlow.repository.UserStoryRepository;
import java.util.List;

import com.ensa.SprintFlow.repository.specification.UserStorySpecification;
import com.ensa.SprintFlow.security.service.UserAuthorizationService;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserStoryService {
  private UserStoryRepository userStoryRepository;
  private UserAuthorizationService userAuthorizationService;
  private UserStoryMapper userStoryMapper;

  public List<UserStoryViewDto> getUserStories(Long projectId, Long epicId, Long sprintId, Boolean unassignedOnly) {
    Specification<UserStory> spec = UserStorySpecification.whereProject( projectId);
    if( epicId != null){
      spec = spec.and( UserStorySpecification.belongsToEpic( epicId));
    }
    if( sprintId != null){
      spec = spec.and( UserStorySpecification.belongsToSprint( sprintId));
    }
    if( unassignedOnly == true){
      spec = spec.and( UserStorySpecification.unassignedOnly());
    }

    List<UserStory> userStoryList = userStoryRepository.findAll( spec);

    return userStoryMapper.mapToUserStoryViewDto( userStoryList);
  }

  public UserStoryResponseDto getUserStory(Long userStoryId) {
    UserStory userStory = findUserStory( userStoryId);
    UserStoryResponseDto userStoryResponseDto =
        userStoryMapper.mapToUserStoryResponseDto(userStory);
    return userStoryResponseDto;
  }

  public void createUserStory(Long projectId, UserStoryRequestDto userStoryDto) {
    UserStory userStory = userStoryMapper.mapToUserStory(userStoryDto);
    Epic defaultEpic = userAuthorizationService.getContextProject().getDefaultEpic();
    userStory.setEpic(defaultEpic);
    userStoryRepository.saveAndFlush(userStory);
  }

  @Transactional
  public void updateUserStory(Long userStoryId, UserStoryRequestDto userStoryDto) {
    UserStory userStory = findUserStory( userStoryId);

    if (userStoryDto.getTitle() != null) {
      userStory.setTitle(userStoryDto.getTitle());
    }

    if (userStoryDto.getPriority() != null) {
      userStory.setPriority(userStoryDto.getPriority());
    }

    if (userStoryDto.getDescription() != null) {
      UserStoryDescription userStoryDescription = userStory.getUserStoryDescription();
      userStoryDescription.setAsWho(userStoryDto.getDescription().getAsWho());
      userStoryDescription.setWhat(userStoryDto.getDescription().getWhat());
      userStoryDescription.setForDesc(userStoryDto.getDescription().getForDesc());
    }
  }

  public void deleteUserStory(Long userStoryId) {
    UserStory userStory = findUserStory( userStoryId);
    userStoryRepository.delete( userStory);
  }

  public List<UserStory> findAllByIds(List<Long> ids) {
    return userStoryRepository.findAllByIds(ids);
  }

  public UserStory findUserStory(Long userStoryId) {
    UserStory userStory = userStoryRepository.findById(userStoryId).orElseThrow(
            () -> new NotFoundException("Nu user story with the given Id")
    );
    return userStory;
  }
}
