package com.ensa.SprintFlow.controller;

import com.ensa.SprintFlow.dto.request.ProjectRequestDto;
import com.ensa.SprintFlow.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class ProjectController {
  private ProjectService productService;

  ProjectController(ProjectService productService) {
    this.productService = productService;
  }

  @PostMapping("/projects")
  public ResponseEntity<?> createProject(@Validated @RequestBody ProjectRequestDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(productService.save(dto));
  }
}
