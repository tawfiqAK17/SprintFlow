package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.dto.projectMember.request.ProjectMemberRequestDto;
import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.exception.generalException.DataIntegrityViolationException;
import com.ensa.SprintFlow.exception.generalException.UnauthorizedException;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.model.ProjectMember;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.repository.ProjectMemberRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProjectMemberService {
  ProjectMemberRepository projectMemberRepository;
  UserService userService;

  public ProjectMember saveAny(Project project, String username, Role role) {
    ProjectMember projectMember = new ProjectMember();
    User user = userService.findByUsername(username);
    if (!projectMemberRepository
        .findByProjectIdAndUserUsernameAndUserRole(project.getId(), username, role)
        .isEmpty()) {
      throw new DataIntegrityViolationException("the user is already a member in the project");
    }
    projectMember.setUser(user);
    projectMember.setProject(project);
    projectMember.setUserRole(role);
    return projectMemberRepository.save(projectMember);
  }

  public void save(Project project, ProjectMemberRequestDto dto) {
    if (dto.getRole().equals(Role.SCRUM_MASTER)) {
      throw new UnauthorizedException(
          "the SCRUM_MASTER should be set via the PUT /projects/{projectId} end point");
    }
    if (dto.getRole().equals(Role.PRODUCT_OWNER)) {
      throw new UnauthorizedException("the PRODUCT_OWNER of a project can not be changed");
    }
    saveAny(project, dto.getUsername(), dto.getRole());
  }

  public User getProjectProductOwner(Long projectId) {
    return findByProjectIdAndUserRole(projectId, Role.PRODUCT_OWNER).getUser();
  }

  public User getProjectScrumMaster(Long projectId) {
    ProjectMember scrumMasterProjectMember =
        findByProjectIdAndUserRole(projectId, Role.SCRUM_MASTER);
    if (scrumMasterProjectMember != null) {
      return scrumMasterProjectMember.getUser();
    }
    return null;
  }

  public List<ProjectMember> getAllMembers(Long projectId) {
    return projectMemberRepository.findAllByProjectId(projectId);
  }

  public List<ProjectMember> findAllByProjectAndUserRole(Long projectId, Role role) {
    return projectMemberRepository.findAllByProjectIdAndUserRole(projectId, role);
  }

  public void deleteProjectScrumMaster(Long projectId) {
    projectMemberRepository.deleteByProjectIdAndUserRole(projectId, Role.SCRUM_MASTER);
  }

  private ProjectMember findByProjectIdAndUserRole(Long projectId, Role role) {
    return projectMemberRepository.findByProjectIdAndUserRole(projectId, role);
  }

  public List<ProjectMember> findAllByProjectIdAndUsername(Long projectId, String username) {
    return projectMemberRepository.findAllByProjectIdAndUserUsername(projectId, username);
  }

  public void deleteRelation(Long id) {
    projectMemberRepository.deleteById(id);
  }

  public List<ProjectMember> findAllByProjectIdAndUserRole(Long projectId, Role role) {
    return projectMemberRepository.findAllByProjectIdAndUserRole(projectId, role);
  }

  public List<ProjectMember> findAllByProjectId(Long projectId) {
    return projectMemberRepository.findAllByProjectId(projectId);
  }
}
