package com.ensa.SprintFlow.service;


import com.ensa.SprintFlow.dto.userStory.request.UserStoryRequestDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryResponseDto;

import com.ensa.SprintFlow.mapper.UserStoryMapper;

import com.ensa.SprintFlow.model.Epic;
import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.model.UserStoryDescription;

import com.ensa.SprintFlow.repository.UserStoryRepository;
import com.ensa.SprintFlow.repository.projection.UserStoryView;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UserStoryService {
    private UserStoryRepository userStoryRepository;
    private ProjectService projectService;
    private UserStoryMapper userStoryMapper;

    public List<UserStoryView> getUserStories( Long epicId, Long sprintId, Boolean unassigned) {
        return userStoryRepository.findAllUserStories( epicId, sprintId, unassigned);
    }

    public UserStoryResponseDto getUserStory(Long userStoryId) {
        UserStory userStory = userStoryRepository.findUserStory(userStoryId);
        UserStoryResponseDto userStoryResponseDto = userStoryMapper.mapToUserStoryResponseDto(userStory);
        return userStoryResponseDto;
    }

    public void createUserStory(Long projectId,UserStoryRequestDto userStoryDto) {
        UserStory userStory = userStoryMapper.mapToUserStory(userStoryDto);
        Epic defaultEpic = projectService.getProject( projectId).getDefaultEpic();
        userStory.setEpic( defaultEpic);
        userStoryRepository.saveAndFlush(userStory);
    }

    @Transactional
    public void updateUserStory(Long userStoryId, UserStoryRequestDto userStoryDto) {
        UserStory userStory = userStoryRepository.findById(userStoryId).orElseThrow();

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
        userStoryRepository.deleteById(userStoryId);
    }

    public UserStory findUserStory(Long userStoryId){
        UserStory userStory = userStoryRepository.findById( userStoryId).orElseThrow();
        return userStory;
    }
}
