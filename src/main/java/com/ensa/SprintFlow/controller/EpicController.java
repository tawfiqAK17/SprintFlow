package com.ensa.SprintFlow.controller;

import com.ensa.SprintFlow.dto.request.EpicCreationRequestDto;
import com.ensa.SprintFlow.dto.request.EpicUpdateRequestDto;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.service.EpicService;
import com.ensa.SprintFlow.service.ProjectService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@AllArgsConstructor
public class EpicController {
  private ProjectService projectService;
  private EpicService epicService;

  @GetMapping("/projects/{projectId}/epics")
  public ResponseEntity<?> getAllEpics(@PathVariable Long projectId) {
    return ResponseEntity.status(HttpStatus.OK).body(epicService.getAllEpics(projectId));
  }

  @PostMapping("/projects/{projectId}/epics")
  public ResponseEntity<?> create(
      @PathVariable Long projectId, @Validated @RequestBody EpicCreationRequestDto dto) {
    Project project = projectService.findById(projectId);
    return ResponseEntity.status(HttpStatus.CREATED).body(epicService.createEpic(project, dto));
  }

  @PostMapping("/projects/{projectId}/epics/{epicId}")
  public ResponseEntity<?> update(
      @PathVariable Long epicId,
      @RequestBody EpicUpdateRequestDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(epicService.update(epicId, dto));
  }
  
  @DeleteMapping("/projects/{projectId}/epics/{epicId}")
  public ResponseEntity<?> delete(@PathVariable Long epicId) {
    epicService.delete(epicId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  } 
}
