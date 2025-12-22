package com.ensa.SprintFlow.controller;

import com.ensa.SprintFlow.builder.ResponseBuilder;
import com.ensa.SprintFlow.dto.request.ProjectRequestDto;
import com.ensa.SprintFlow.dto.request.ProjectUpdateRequestDto;
import com.ensa.SprintFlow.dto.response.ProjectMetaDataResponseDto;
import com.ensa.SprintFlow.service.ProjectService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@AllArgsConstructor
public class ProjectController {
  private ProjectService projectService;
  private ResponseBuilder responseBuilder;

  @PostMapping("/projects")
  public ResponseEntity<?> createProject(@Validated @RequestBody ProjectRequestDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(projectService.save(dto));
  }

  @PutMapping("/projects/{id}")
  public ResponseEntity<?> updateProject(
      @PathVariable Long id, @RequestBody ProjectUpdateRequestDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(projectService.update(id, dto));
  }

  @GetMapping("/projects")
  public ResponseEntity<?> getProjects() {
    List<ProjectMetaDataResponseDto> projects = projectService.getProjects();
    return responseBuilder.status(HttpStatus.OK).property("projects", projects).build();
  }

  @GetMapping("/projects/{id}")
  public ResponseEntity<?> getProject(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.CREATED).body(projectService.getProject(id));
  }
}
