package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.request.ProjectRequestDto;
import com.ensa.SprintFlow.dto.response.ProjectMetaDataResponseDto;
import com.ensa.SprintFlow.dto.response.ProjectResponseDto;
import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.model.ProjectMember;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.service.ProjectMemberService;
import java.util.List;
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
    if (project == null) {
      return null;
    }
    ProjectMetaDataResponseDto dto = new ProjectMetaDataResponseDto();
    dto.setName(project.getName());
    dto.setId(project.getId());
    dto.setDescription(project.getDescription());
    User scrumMaster = projectMemberService.getProjectScrumMaster(project.getId());
    User productOwner = projectMemberService.getProjectProductOwner(project.getId());
    if (scrumMaster != null) {
      dto.setScrumMaster(userMapper.mapToMetaDataDto(scrumMaster));
    }
    if (productOwner != null) {
      dto.setProductOwner(userMapper.mapToMetaDataDto(productOwner));
    }
    dto.setUserRole(Role.PRODUCT_OWNER);
    return dto;
  }

  public ProjectResponseDto mapToResponseDto(Project project) {
    if (project == null) {
      return null;
    }
    ProjectResponseDto dto = new ProjectResponseDto();
    dto.setId(project.getId());
    dto.setName(project.getName());
    dto.setDescription(project.getDescription());
    dto.setCreationDate(project.getCreationDate());
    dto.setScrumMaster(
        userMapper.mapToMetaDataDto(projectMemberService.getProjectScrumMaster(project.getId())));
    dto.setProductOwner(
        userMapper.mapToMetaDataDto(projectMemberService.getProjectProductOwner(project.getId())));
    // get all the member except the scrum master and the product owner
    List<ProjectMember> projectMembers =
        projectMemberService.getAllMembers(project.getId()).stream()
            .filter(
                m ->
                    !m.getUserRole().equals(Role.SCRUM_MASTER)
                        && !m.getUserRole().equals(Role.PRODUCT_OWNER))
            .toList();
    // map the relation to a ProjectResponseDto.Member
    List<ProjectResponseDto.Member> members =
        projectMembers.stream()
            .map(
                m ->
                    new ProjectResponseDto.Member(
                        userMapper.mapToMetaDataDto(m.getUser()), m.getUserRole()))
            .toList();
    dto.setMembers(members);
    return dto;
  }
}
