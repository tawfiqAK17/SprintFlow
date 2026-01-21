package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.userStoryDescription.UserStoryDescriptionDto;
import com.ensa.SprintFlow.model.UserStoryDescription;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserStoryDescriptionMapper {

    UserStoryDescriptionDto mapToUserStoryDescriptionDto(UserStoryDescription userStoryDescription);

    UserStoryDescription mapToUserStoryDescription(UserStoryDescriptionDto dto);
}
