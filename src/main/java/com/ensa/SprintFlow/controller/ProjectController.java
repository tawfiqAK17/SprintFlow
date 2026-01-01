package com.ensa.SprintFlow.controller;

import com.ensa.SprintFlow.builder.ResponseBuilder;
import com.ensa.SprintFlow.dto.project.request.ProjectRequestDto;
import com.ensa.SprintFlow.dto.project.request.ProjectUpdateRequestDto;
import com.ensa.SprintFlow.dto.project.response.ProjectMetaDataResponseDto;
import com.ensa.SprintFlow.dto.project.response.ProjectResponseDto;
import com.ensa.SprintFlow.mapper.ProjectMapper;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeMember;
import com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeProductOwner;
import com.ensa.SprintFlow.service.ProjectService;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@AllArgsConstructor
public class ProjectController {
  private ProjectMapper mapper;

  private ProjectService projectService;
  private ResponseBuilder responseBuilder;

  @PostMapping("/projects")
  public ResponseEntity<?> createProject(@Validated @RequestBody ProjectRequestDto dto) {
    Project project = projectService.save(dto);
    ProjectMetaDataResponseDto projectResponse = mapper.mapToMetaDataResponseDto(project);
    return ResponseEntity.status(HttpStatus.CREATED).body(projectResponse);
  }

  @AuthorizeProductOwner
  @PutMapping("/projects/{id}")
  public ResponseEntity<?> updateProject(
      @PathVariable Long id, @RequestBody ProjectUpdateRequestDto dto) {
    Project project = projectService.update(id, dto);
    ProjectMetaDataResponseDto projectResponse = mapper.mapToMetaDataResponseDto(project);
    return ResponseEntity.status(HttpStatus.OK).body(projectResponse);
  }

  @GetMapping("/projects")
  public ResponseEntity<?> getProjects() {
    List<Project> projects = projectService.getProjects();
    List<ProjectMetaDataResponseDto> projectsMetaData = new ArrayList<>();
    for (Project project : projects) {
      projectsMetaData.add(mapper.mapToMetaDataResponseDto(project));
    }
    return responseBuilder.status(HttpStatus.OK).property("projects", projectsMetaData).build();
  }

  @AuthorizeMember
  @GetMapping("/projects/{id}")
  public ResponseEntity<?> getProject(@PathVariable Long id) {
    Project project = projectService.getProject(id);
    ProjectResponseDto projectResponse = mapper.mapToResponseDto(project);
    return ResponseEntity.status(HttpStatus.CREATED).body(projectResponse);
  }

  @AuthorizeProductOwner
  @DeleteMapping("/projects/{id}")
  public ResponseEntity<?> deleteProject(@PathVariable Long id) {
    projectService.deleteProject(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
