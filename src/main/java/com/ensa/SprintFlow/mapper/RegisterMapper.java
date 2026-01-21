package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.security.dto.request.RegisterRequestDto;
import com.ensa.SprintFlow.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterMapper {

    @Mapping(target = "verified", constant = "false")
    User mapToUser(RegisterRequestDto dto);
}
