package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.dto.project.request.ProjectRequestDto;
import com.ensa.SprintFlow.dto.project.request.ProjectUpdateRequestDto;
import com.ensa.SprintFlow.dto.projectMember.request.ProjectMemberRequestDto;
import com.ensa.SprintFlow.dto.projectMember.response.ProjectMemberResponseDto;
import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.mapper.ProjectMapper;
import com.ensa.SprintFlow.mapper.ProjectMemberMapper;
import com.ensa.SprintFlow.model.Epic;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.model.ProjectMember;
import com.ensa.SprintFlow.repository.ProjectRepository;
import com.ensa.SprintFlow.security.model.UserContext;
import com.ensa.SprintFlow.util.Utils;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectService {
  private ProjectMapper mapper;

  private ProjectRepository projectRepository;
  ProjectMemberService projectMemberService;
  private EpicService epicService;
  private ProjectMemberMapper projectMemberMapper;

  @Transactional
  public Project save(ProjectRequestDto dto) {
    Project project = mapper.mapToProject(dto);
    project.setCreationDate(LocalDateTime.now());
    // save the project to the database
    project = projectRepository.save(project);
    // extract the user from the security context
    UserContext userContext =
        (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    // save the user as the product owner of the project
    projectMemberService.saveAny(project, userContext.getUsername(), Role.PRODUCT_OWNER);
    // save the scrum master relation
    projectMemberService.saveAny(project, dto.getScrumMasterUsername(), Role.SCRUM_MASTER);

    // create the default project epic
    Epic defaultEpic =
        epicService.save(
            Epic.builder()
                .title("Global Epic")
                .description("the default epic for the project")
                .project( project)
                .build());
    project.setDefaultEpic(defaultEpic);
    return project;
  }

  public Project update(Long projectId, ProjectUpdateRequestDto dto) {
    Project project = findById(projectId);
    if (dto.getName() != null) {
      project.setName(dto.getName());
    }
    if (dto.getDescription() != null) {
      project.setDescription(dto.getDescription());
    }
    if (dto.getScrumMasterUsername() != null) {
      replaceScrumMaster(project, dto.getScrumMasterUsername());
    }
    // save the project to the database
    project = projectRepository.save(project);
    return project;
  }

  public List<Project> getProjects() {
    List<Project> projects = projectRepository.findAll();
    return projects;
  }

  public Project getProject(Long projectId) {
    Project project = findById(projectId);
    return project;
  }

  public Project findById(Long projectId) {
    Optional<Project> optionalProject = projectRepository.findById(projectId);
    if (optionalProject.isEmpty()) {
      throw new NotFoundException("no project found with the given id");
    }
    return optionalProject.get();
  }

  private void replaceScrumMaster(Project project, String scrumMasterUsername) {
    projectMemberService.deleteProjectScrumMaster(project.getId());
    projectMemberService.saveAny(project, scrumMasterUsername, Role.SCRUM_MASTER);
  }

  public void deleteProject(Long id) {
    projectRepository.deleteById(id);
  }

  public void saveMember(Long projectId, ProjectMemberRequestDto dto) {
    Project project = findById(projectId);
    projectMemberService.save(project, dto);
  }

  public void addRoleToMember(Long projectId, String username, Role role) {
    List<ProjectMember> projectMembers =
        projectMemberService.findAllByProjectIdAndUsername(projectId, username);
    if (!projectMembers.stream().anyMatch(p -> p.getUser().getUsername().equals(username))) {
      throw new NotFoundException("the project does not have any member with the given username");
    }
    projectMemberService.save(
        Utils.getUserContext().getProject(),
        ProjectMemberRequestDto.builder().username(username).role(role).build());
  }

  public void removeRoleFromMember(Long projectId, String username, Role role) {
    List<ProjectMember> projectMembers =
        projectMemberService.findAllByProjectIdAndUsername(projectId, username);
    ProjectMember projectMember =
        projectMembers.stream()
            .filter(p -> p.getUser().getUsername().equals(username) && p.getUserRole().equals(role))
            .findFirst()
            .orElseThrow(
                () ->
                    new NotFoundException(
                        "the project does not have any member with the given username and role"));
    projectMemberService.deleteRelation(projectMember.getId());
  }

  public void removeMember(Long projectId, String username) {
    List<ProjectMember> memberRelations =
        projectMemberService.findAllByProjectIdAndUsername(projectId, username);
    memberRelations.stream().forEach(m -> projectMemberService.deleteRelation(m.getId()));
  }

  public List<ProjectMemberResponseDto> getProjectMembers(Long projectId, Role role) {
    if (role != null) {
      return projectMemberService.findAllByProjectIdAndUserRole(projectId, role).stream()
          .map(p -> projectMemberMapper.mapToProjectMemberResponseDto(p))
          .toList();
    }
    return projectMemberService.findAllByProjectId(projectId).stream()
        .map(p -> projectMemberMapper.mapToProjectMemberResponseDto(p))
        .toList();
  }
}
