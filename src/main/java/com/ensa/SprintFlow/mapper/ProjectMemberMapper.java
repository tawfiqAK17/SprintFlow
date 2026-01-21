package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.projectMember.response.ProjectMemberResponseDto;
import com.ensa.SprintFlow.model.ProjectMember;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProjectMemberMapper {

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "role", source = "userRole")
    ProjectMemberResponseDto mapToProjectMemberResponseDto(ProjectMember projectMember);
}
