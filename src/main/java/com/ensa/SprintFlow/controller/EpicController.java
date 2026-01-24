package com.ensa.SprintFlow.controller;

import com.ensa.SprintFlow.dto.epic.request.EpicRequestDto;
import com.ensa.SprintFlow.dto.epic.response.EpicResponseDto;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeMember;
import com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeProductOwner;
import com.ensa.SprintFlow.service.EpicService;
import com.ensa.SprintFlow.service.ProjectService;
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

@RestController
@AllArgsConstructor
public class EpicController {
  private ProjectService projectService;
  private EpicService epicService;

  @AuthorizeMember
  @GetMapping("/projects/{projectId}/epics")
  public ResponseEntity<?> getAllEpics(@PathVariable Long projectId) {
    List<EpicResponseDto> epics = epicService.getAllEpics(projectId);
    return ResponseEntity.status(epics.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK)
        .body(epics);
  }

  @AuthorizeMember
  @GetMapping("/projects/{projectId}/epics/{epicId}")
  public ResponseEntity<?> getEpic(@PathVariable Long projectId, @PathVariable Long epicId) {
    return ResponseEntity.status(HttpStatus.OK).body(epicService.getEpic(epicId));
  }

  @AuthorizeProductOwner
  @PostMapping("/projects/{projectId}/epics")
  public ResponseEntity<?> create(
      @PathVariable Long projectId, @Validated @RequestBody EpicRequestDto dto) {
    Project project = projectService.findById(projectId);
    return ResponseEntity.status(HttpStatus.CREATED).body(epicService.createEpic(project, dto));
  }

  @AuthorizeProductOwner
  @PutMapping("/projects/{projectId}/epics/{epicId}")
  public ResponseEntity<?> update(
      @PathVariable Long projectId, @PathVariable Long epicId, @RequestBody EpicRequestDto dto) {
    return ResponseEntity.status(HttpStatus.OK).body(epicService.update(epicId, dto));
  }

  @AuthorizeProductOwner
  @DeleteMapping("/projects/{projectId}/epics/{epicId}")
  public ResponseEntity<?> delete(@PathVariable Long projectId, @PathVariable Long epicId) {
    epicService.delete(epicId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @AuthorizeProductOwner
  @PostMapping("/projects/{projectId}/epics/{epicId}/user_stories")
  public ResponseEntity<?> addUserStories(
      @PathVariable Long projectId,
      @PathVariable Long epicId,
      @RequestBody List<Long> userStoriesIds) {
    epicService.addUserStories(epicId, userStoriesIds);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @AuthorizeProductOwner
  @DeleteMapping("/projects/{projectId}/epics/{epicId}/user_stories/{userStoryId}")
  public ResponseEntity<?> removeUserStory(
      @PathVariable Long projectId, @PathVariable Long epicId, @PathVariable Long userStoryId) {
    epicService.removeUserStory(epicId, userStoryId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
