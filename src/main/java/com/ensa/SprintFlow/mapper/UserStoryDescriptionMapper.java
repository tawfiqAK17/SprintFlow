package com.ensa.SprintFlow.mapper;


import com.ensa.SprintFlow.dto.userStoryDescription.UserStoryDescriptionDto;
import com.ensa.SprintFlow.model.UserStoryDescription;
import org.springframework.stereotype.Component;

@Component
public class UserStoryDescriptionMapper {

    public UserStoryDescriptionDto mapToUserStoryDescriptionDto(UserStoryDescription userStoryDescription){
        if (userStoryDescription == null){
            return null;
        }
        return UserStoryDescriptionDto.builder()
                .asWho( userStoryDescription.getAsWho())
                .what( userStoryDescription.getWhat())
                .forDesc( userStoryDescription.getForDesc())
                .build();
    }

    public UserStoryDescription mapToUserStoryDescription(UserStoryDescriptionDto dto){
        if (dto == null){
            return null;
        }
        return UserStoryDescription.builder()
                .asWho( dto.getAsWho())
                .what( dto.getWhat())
                .forDesc( dto.getForDesc())
                .build();
    }
}
