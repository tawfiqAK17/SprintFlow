package com.ensa.SprintFlow.controller;

import com.ensa.SprintFlow.dto.sprint.request.SprintRequestDto;
import com.ensa.SprintFlow.dto.sprint.response.SprintResponseDto;
import com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeMember;
import com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeScrumMaster;
import com.ensa.SprintFlow.service.SprintService;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class SprintController {
  SprintService sprintService;

  @AuthorizeScrumMaster
  @GetMapping("/projects/{projectId}/sprints/{sprintId}/chart")
  public ResponseEntity<?> getSprintChart(
      @PathVariable Long projectId, @PathVariable Long sprintId) {
    return ResponseEntity.status(HttpStatus.OK).body(sprintService.getBurndownChart(sprintId));
  }

  @AuthorizeMember
  @GetMapping("/projects/{projectId}/sprints")
  public ResponseEntity<?> getAllSprints(
      @PathVariable Long projectId,
      @RequestParam(name = "startDate", required = false) LocalDateTime startDate,
      @RequestParam(name = "endDate", required = false) LocalDateTime endDate) {
    List<SprintResponseDto> sprints = sprintService.getAllSprints(projectId, startDate, endDate);
    return ResponseEntity.status(sprints.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK)
        .body(sprints);
  }

  @AuthorizeMember
  @GetMapping("/projects/{projectId}/sprints/{sprintId}")
  public ResponseEntity<?> getSprint(@PathVariable Long projectId, @PathVariable Long sprintId) {
    return ResponseEntity.status(HttpStatus.OK).body(sprintService.getSprint(sprintId));
  }

  @AuthorizeScrumMaster
  @PostMapping("/projects/{projectId}/sprints")
  public ResponseEntity<?> create(
      @PathVariable Long projectId, @Validated @RequestBody SprintRequestDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(sprintService.save(dto));
  }

  @AuthorizeScrumMaster
  @PutMapping("/projects/{projectId}/sprints/{sprintId}")
  public ResponseEntity<?> update(
      @PathVariable Long porjectId,
      @PathVariable Long sprintId,
      @RequestBody SprintRequestDto dto) {
    return ResponseEntity.status(HttpStatus.OK).body(sprintService.update(sprintId, dto));
  }

  @AuthorizeScrumMaster
  @DeleteMapping("/projects/{projectId}/sprints/{sprintId}")
  public ResponseEntity<?> delete(@PathVariable Long projectId, @PathVariable Long sprintId) {
    sprintService.delete(sprintId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @AuthorizeScrumMaster
  @PostMapping("/projects/{projectId}/sprints/{sprintId}/user_stories")
  public ResponseEntity<?> addUserStories(
      @PathVariable Long projectId,
      @PathVariable Long sprintId,
      @RequestBody List<Long> userStoriesIds) {
    sprintService.addUserStories(sprintId, userStoriesIds);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @AuthorizeScrumMaster
  @DeleteMapping("/projects/{projectId}/sprints/{sprintId}/user_stories/{userStoryId}")
  public ResponseEntity<?> removeUserStory(
      @PathVariable Long projectId, @PathVariable Long sprintId, @PathVariable Long userStoryId) {
    sprintService.removeUserStory(sprintId, userStoryId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }
}
