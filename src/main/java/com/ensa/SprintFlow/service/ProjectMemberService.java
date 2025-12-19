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

  public ProjectMember findByProjectAndUserRole(Project project, Role role) {
    return projectMemberRepository.findByProjectAndUserRole(project, role);
  }

  public List<ProjectMember> findAllByUserIdAndProjectId(Long userId, Long projectId) {
    return projectMemberRepository.findAllByUserIdAndProjectId(userId, projectId);
  }

  public void deleteProjectScrumMaster(Long projectId) {
    projectMemberRepository.deleteByProjectIdAndUserRole(projectId, Role.SCRUM_MASTER);
  }
}
