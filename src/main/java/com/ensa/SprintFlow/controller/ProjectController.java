package com.ensa.SprintFlow.controller;

import com.ensa.SprintFlow.builder.ResponseBuilder;
import com.ensa.SprintFlow.dto.request.ProjectRequestDto;
import com.ensa.SprintFlow.dto.response.ProjectMetaDataResponseDto;
import com.ensa.SprintFlow.service.ProjectService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class ProjectController {
  private ProjectService productService;
  private ResponseBuilder responseBuilder;

  ProjectController(ProjectService productService, ResponseBuilder responseBuilder) {
    this.productService = productService;
    this.responseBuilder = responseBuilder;
  }

  @PostMapping("/projects")
  public ResponseEntity<?> createProject(@Validated @RequestBody ProjectRequestDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(dto));
  }

  @GetMapping("/projects")
  public ResponseEntity<?> getProjects() {
    List<ProjectMetaDataResponseDto> projects = productService.getProjects();
    return responseBuilder.status(HttpStatus.OK).property("projects", projects).build();
  }
}
