package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.userStory.request.UserStoryRequestDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryMetaDataResponseDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryResponseDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryViewDto;
import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.model.UserStoryPriority;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring", uses = {EpicMapper.class, SprintMapper.class, UserStoryDescriptionMapper.class, AcceptanceCriteriaMapper.class})
public interface UserStoryMapper {

    @Mapping(target = "description", source = "userStory.userStoryDescription")
    @Mapping(target = "priority", source = "priority")
    UserStoryResponseDto mapToUserStoryResponseDto(UserStory userStory, UserStoryPriority priority);

    UserStoryMetaDataResponseDto mapToUserStoryMetaDataResponseDto(UserStory userStory);

    @Mapping(target = "userStoryDescription", source = "description")
    UserStory mapToUserStory(UserStoryRequestDto dto);

    @Mapping(target = "priority", source = "priority")
    UserStoryViewDto mapToUserStoryViewDto(UserStory userStory, UserStoryPriority priority);

    default List<UserStoryViewDto> mapToUserStoryViewDto(List<UserStory> userStories,
                                                         List<UserStoryPriority> priorities){

      List<UserStoryViewDto> userStoryViewDtoList = new ArrayList<>();

      if (userStories == null || priorities == null) {
        return userStoryViewDtoList;
      }

      for( int i = 0; i < userStories.size(); i++){
        UserStory userStory = userStories.get(i);
        UserStoryPriority priority = priorities.get(i);
        userStoryViewDtoList.add( mapToUserStoryViewDto( userStory, priority));
      }
      return userStoryViewDtoList;
    }
}