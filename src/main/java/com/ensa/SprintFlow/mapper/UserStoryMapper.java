package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.userStory.request.UserStoryRequestDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryMetaDataResponseDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryResponseDto;
import com.ensa.SprintFlow.model.UserStory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EpicMapper.class, SprintMapper.class, UserStoryDescriptionMapper.class, AcceptanceCriteriaMapper.class})
public interface UserStoryMapper {

    @Mapping(target = "description", source = "userStoryDescription")
    UserStoryResponseDto mapToUserStoryResponseDto(UserStory userStory);

    UserStoryMetaDataResponseDto mapToUserStoryMetaDataResponseDto(UserStory userStory);

    @Mapping(target = "userStoryDescription", source = "description")
    UserStory mapToUserStory(UserStoryRequestDto dto);
}
