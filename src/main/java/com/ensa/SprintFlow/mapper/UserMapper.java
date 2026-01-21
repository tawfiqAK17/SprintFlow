package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.user.response.UserMetaDataResponseDto;
import com.ensa.SprintFlow.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMetaDataResponseDto mapToMetaDataDto(User user);
}
