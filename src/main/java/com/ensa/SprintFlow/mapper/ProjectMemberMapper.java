package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.projectMember.response.ProjectMemberResponseDto;
import com.ensa.SprintFlow.model.ProjectMember;
import com.ensa.SprintFlow.model.User;
import org.springframework.stereotype.Component;

@Component
public class ProjectMemberMapper {
  public ProjectMemberResponseDto mapToProjectMemberResponseDto(ProjectMember projectMember) {
    User user = projectMember.getUser();
    return ProjectMemberResponseDto.builder()
        .username(user.getUsername())
        .firstName(user.getFirstName())
        .email(user.getEmail())
        .role(projectMember.getUserRole())
        .build();
  }
}
