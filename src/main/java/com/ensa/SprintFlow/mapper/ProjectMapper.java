package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.request.ProjectRequestDto;
import com.ensa.SprintFlow.dto.response.ProjectMetaDataResponseDto;
import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.model.ProjectMember;
import com.ensa.SprintFlow.service.ProjectMemberService;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProjectMapper {

  UserMapper userMapper;
  ProjectMemberService projectMemberService;

  public Project mapToProject(ProjectRequestDto dto) {
    Project project = new Project();
    project.setName(dto.getName());
    project.setDescription(dto.getDescription());
    return project;
  }

  public ProjectMetaDataResponseDto mapToMetaDataResponseDto(Project project) {
    ProjectMetaDataResponseDto dto = new ProjectMetaDataResponseDto();
    dto.setName(project.getName());
    dto.setId(project.getId());
    dto.setDescription(project.getDescription());
    ProjectMember scrumMaster =
        projectMemberService.findByProjectAndUserRole(project, Role.SCRUM_MASTER);
    ProjectMember productOwner =
        projectMemberService.findByProjectAndUserRole(project, Role.PRODUCT_OWNER);
    if (scrumMaster != null) {
      dto.setScrumMaster(userMapper.mapToMetaDataDto(scrumMaster.getUser()));
    }
    if (productOwner != null) {
      dto.setProductOwner(userMapper.mapToMetaDataDto(productOwner.getUser()));
    }
    dto.setUserRole(Role.PRODUCT_OWNER);
    return dto;
  }
}
