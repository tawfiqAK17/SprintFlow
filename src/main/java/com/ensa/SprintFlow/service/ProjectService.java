package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.dto.request.ProjectRequestDto;
import com.ensa.SprintFlow.dto.response.ProjectMetaDataResponseDto;
import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.mapper.ProjectMapper;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.model.security.CustomUserDetails;
import com.ensa.SprintFlow.repository.ProjectRepository;
import jakarta.transaction.Transactional;
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
    // save the project to the database
    project = projectRepository.save(project);
    // extract the user from the security context
    CustomUserDetails userDetails =
        (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    // save the user as the product owner of the project
    projectMemberService.save(project, userDetails.getUsername(), Role.PRODUCT_OWNER);
    // if the scrum master was provided in the request than we save the relation
    if (dto.getScrumMasterUsername() != null) {
      projectMemberService.save(project, dto.getScrumMasterUsername(), Role.SCRUM_MASTER);
    }
    return mapper.mapToMetaDataResponseDto(project);
  }
}
