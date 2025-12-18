package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.dto.request.ProjectRequestDto;
import com.ensa.SprintFlow.dto.response.ProjectMetaDataResponseDto;
import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.mapper.ProjectMapper;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.model.security.UserContext;
import com.ensa.SprintFlow.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

  private ProjectRepository projectRepository;
  ProjectMemberService projectMemberService;
  private ProjectMapper mapper;

  ProjectService(
      ProjectRepository projectRepository,
      ProjectMemberService projectMemberService,
      ProjectMapper mapper) {
    this.projectRepository = projectRepository;
    this.projectMemberService = projectMemberService;
    this.mapper = mapper;
  }

  @Transactional
  public ProjectMetaDataResponseDto save(ProjectRequestDto dto) {
    Project project = mapper.mapToProject(dto);
    project.setCreationDate(LocalDateTime.now());
    // save the project to the database
    project = projectRepository.save(project);
    // extract the user from the security context
    UserContext userContext =
        (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    // save the user as the product owner of the project
    projectMemberService.save(project, userContext.getUsername(), Role.PRODUCT_OWNER);
    // if the scrum master was provided in the request than we save the relation
    if (dto.getScrumMasterUsername() != null) {
      projectMemberService.save(project, dto.getScrumMasterUsername(), Role.SCRUM_MASTER);
    }
    return mapper.mapToMetaDataResponseDto(project);
  }

  public ProjectMetaDataResponseDto update(Long projectId, ProjectRequestDto dto) {
    Optional<Project> OptionalProject = projectRepository.findById(projectId);
    if (OptionalProject.isEmpty()) {
      throw new NotFoundException("no project found with the given id");
    }
    Project project = OptionalProject.get();
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
    return mapper.mapToMetaDataResponseDto(project);
  }

  public List<ProjectMetaDataResponseDto> getProjects() {
    List<Project> projects = projectRepository.findAll();
    List<ProjectMetaDataResponseDto> projectsMetaData = new ArrayList<>();
    for (Project project : projects) {
      projectsMetaData.add(mapper.mapToMetaDataResponseDto(project));
    }
    return projectsMetaData;
  }

  private void replaceScrumMaster(Project project, String scrumMasterUsername) {
    projectMemberService.deleteProjectScrumMaster(project.getId());
    projectMemberService.save(project, scrumMasterUsername, Role.SCRUM_MASTER);
  }
}
