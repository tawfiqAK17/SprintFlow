package com.ensa.SprintFlow.controller;

import com.ensa.SprintFlow.builder.CustomResponseBuilder;
import com.ensa.SprintFlow.dto.project.request.ProjectRequestDto;
import com.ensa.SprintFlow.dto.project.request.ProjectUpdateRequestDto;
import com.ensa.SprintFlow.dto.project.response.ProjectMetaDataResponseDto;
import com.ensa.SprintFlow.dto.projectMember.request.ProjectMemberRequestDto;
import com.ensa.SprintFlow.dto.projectMember.response.ProjectMemberResponseDto;
import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeMember;
import com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeProductOwner;
import com.ensa.SprintFlow.security.annotation.projectAuthorization.AuthorizeScrumMaster;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@AllArgsConstructor
public class ProjectController {

  private ProjectService projectService;
  private CustomResponseBuilder responseBuilder;

  @PostMapping("/projects")
  public ResponseEntity<?> createProject(@Validated @RequestBody ProjectRequestDto dto) {
    return ResponseEntity.status(HttpStatus.CREATED).body(projectService.save(dto));
  }

  @AuthorizeProductOwner
  @PutMapping("/projects/{projectId}")
  public ResponseEntity<?> updateProject(
      @PathVariable Long projectId, @RequestBody ProjectUpdateRequestDto dto) {
    return ResponseEntity.status(HttpStatus.OK).body(projectService.update(projectId, dto));
  }

  @GetMapping("/projects")
  public ResponseEntity<?> getProjects() {
    List<ProjectMetaDataResponseDto> projects = projectService.getProjects();
    return responseBuilder
        .status(projects.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK)
        .property("projects", projects)
        .build();
  }

  @AuthorizeMember
  @GetMapping("/projects/{id}")
  public ResponseEntity<?> getProject(@PathVariable Long id) {
    return ResponseEntity.status(HttpStatus.CREATED).body(projectService.getProject(id));
  }

  @AuthorizeProductOwner
  @DeleteMapping("/projects/{id}")
  public ResponseEntity<?> deleteProject(@PathVariable Long id) {
    projectService.deleteProject(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @AuthorizeMember
  @GetMapping("/projects/{projectId}/members")
  public ResponseEntity<?> getAllMembers(@RequestParam(name = "role", required = false) Role role, @PathVariable Long projectId) {
    List<ProjectMemberResponseDto> members = projectService.getProjectMembers(projectId, role);
    return ResponseEntity.status(members.isEmpty() ? HttpStatus.NO_CONTENT : HttpStatus.OK)
        .body(members);
  }

  @AuthorizeScrumMaster
  @PostMapping("/projects/{projectId}/members")
  public ResponseEntity<?> addMember(
      @PathVariable Long projectId, @RequestBody ProjectMemberRequestDto dto) {
    projectService.saveMember(projectId, dto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @AuthorizeScrumMaster
  @PutMapping("/projects/{projectId}/members/{username}/roles")
  public ResponseEntity<?> addRoleToMember(
      @PathVariable Long projectId, @PathVariable String username, @RequestBody Role role) {
    projectService.addRoleToMember(projectId, username, role);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @AuthorizeScrumMaster
  @DeleteMapping("/projects/{projectId}/members/{username}/roles")
  public ResponseEntity<?> removeRoleFromMember(
      @PathVariable Long projectId, @PathVariable String username, @RequestBody Role role) {
    projectService.removeRoleFromMember(projectId, username, role);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @AuthorizeScrumMaster
  @DeleteMapping("/projects/{projectId}/members/{username}")
  public ResponseEntity<?> removeMember(
      @PathVariable Long projectId, @PathVariable String username) {
    projectService.removeMember(projectId, username);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
