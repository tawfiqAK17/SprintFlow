package com.ensa.SprintFlow.service;


import com.ensa.SprintFlow.dto.userStory.request.UserStoryRequestDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryMetaDataResponseDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryResponseDto;

import com.ensa.SprintFlow.mapper.UserStoryMapper;

import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.model.UserStoryDescription;

import com.ensa.SprintFlow.repository.UserStoryRepository;
import com.ensa.SprintFlow.repository.projection.UserStoryView;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserStoryService {
    private UserStoryRepository userStoryRepository;
    private UserStoryMapper userStoryMapper;

    public List<UserStoryMetaDataResponseDto> getUserStories(){
        List<UserStoryMetaDataResponseDto> response = new ArrayList<>();

        for( UserStoryView userStory : userStoryRepository.findAllUserStories()){
            response.add( userStoryMapper.mapToUserStoryMetaDataResponseDto( userStory));
        }

        return response;
    }

    public UserStoryResponseDto getUserStory(Long userStoryId){
        UserStory userStory = userStoryRepository.findUserStory( userStoryId);
        System.out.println( userStory.getTitle());
        UserStoryResponseDto userStoryResponseDto = userStoryMapper.mapToUserStoryResponseDto( userStory );
        return userStoryResponseDto;
    }

    public void createUserStory(UserStoryRequestDto userStoryDto){

        UserStory userStory = userStoryMapper.mapToUserStory( userStoryDto);
        System.out.println(userStory);
        userStoryRepository.saveAndFlush( userStory);
    }

    @Transactional
    public void updateUserStory(Long userStoryId, UserStoryRequestDto userStoryDto){
        UserStory userStory = userStoryRepository.findById( userStoryId).orElseThrow();

        if( userStoryDto.getTitle() != null){
            userStory.setTitle( userStoryDto.getTitle());
        }

        if( userStoryDto.getPriority() != null){
            userStory.setPriority( userStoryDto.getPriority());
        }

        if( userStoryDto.getDescription() != null){
            UserStoryDescription userStoryDescription = userStory.getUserStoryDescription();
            userStoryDescription.setAsWho( userStoryDto.getDescription().getAsWho());
            userStoryDescription.setWhat( userStoryDto.getDescription().getWhat());
            userStoryDescription.setForDesc( userStoryDto.getDescription().getForDesc());
        }
    }

    public void deleteUserStory( Long userStoryId){
        userStoryRepository.deleteById( userStoryId);
    }

    public List<UserStory> findAllByIds(List<Long> ids) {
      return userStoryRepository.findAllByIds(ids);
    }
}
