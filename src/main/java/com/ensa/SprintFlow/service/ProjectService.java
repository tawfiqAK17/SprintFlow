package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.dto.request.ProjectRequestDto;
import com.ensa.SprintFlow.dto.request.ProjectUpdateRequestDto;
import com.ensa.SprintFlow.dto.response.ProjectMetaDataResponseDto;
import com.ensa.SprintFlow.dto.response.ProjectResponseDto;
import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.mapper.ProjectMapper;
import com.ensa.SprintFlow.model.Epic;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.model.security.UserContext;
import com.ensa.SprintFlow.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectService {

  private ProjectRepository projectRepository;
  ProjectMemberService projectMemberService;
  private ProjectMapper mapper;
  private EpicService epicService;

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
    // save the scrum master relation
    projectMemberService.save(project, dto.getScrumMasterUsername(), Role.SCRUM_MASTER);

    // create the default project epic
    Epic defaultEpic =
        epicService.save(
            Epic.builder()
                .title("Global Epic")
                .description("the default epic for the project")
                .build());
    project.setDefaultEpic(defaultEpic);
    return mapper.mapToMetaDataResponseDto(project);
  }

  public ProjectMetaDataResponseDto update(Long projectId, ProjectUpdateRequestDto dto) {
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

  public ProjectResponseDto getProject(Long projectId) {
    Project project = findById(projectId);
    return mapper.mapToResponseDto(project);
  }

  private void replaceScrumMaster(Project project, String scrumMasterUsername) {
    projectMemberService.deleteProjectScrumMaster(project.getId());
    projectMemberService.save(project, scrumMasterUsername, Role.SCRUM_MASTER);
  }

  private Project findById(Long projectId) {
    Optional<Project> optionalProject = projectRepository.findById(projectId);
    if (optionalProject.isEmpty()) {
      throw new NotFoundException("no project found with the given id");
    }
    return optionalProject.get();
  }

  public void deleteProject(Long id) {
    projectRepository.deleteById(id);
  }
}
