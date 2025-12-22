package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.enums.Role;
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

  public ProjectMember save(Project project, String username, Role role) {
    ProjectMember projectMember = new ProjectMember();
    User user = userService.findByUsername(username);
    projectMember.setUser(user);
    projectMember.setProject(project);
    projectMember.setUserRole(role);
    return projectMemberRepository.save(projectMember);
  }

  public User getProjectProductOwner(Long projectId) {
    return findByProjectIdAndUserRole(projectId, Role.PRODUCT_OWNER).getUser();
  }

  public User getProjectScrumMaster(Long projectId) {
    ProjectMember scrumMasterProjectMember = findByProjectIdAndUserRole(projectId, Role.SCRUM_MASTER);
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

  public List<ProjectMember> findAllByUserIdAndProjectId(Long userId, Long projectId) {
    return projectMemberRepository.findAllByUserIdAndProjectId(userId, projectId);
  }

  public void deleteProjectScrumMaster(Long projectId) {
    projectMemberRepository.deleteByProjectIdAndUserRole(projectId, Role.SCRUM_MASTER);
  }

  private ProjectMember findByProjectIdAndUserRole(Long projectId, Role role) {
    return projectMemberRepository.findByProjectIdAndUserRole(projectId, role);
  }
}
