package com.ensa.SprintFlow.security.service;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.model.ProjectMember;
import com.ensa.SprintFlow.security.model.UserContext;
import com.ensa.SprintFlow.service.ProjectMemberService;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserAuthorizationService {
  ProjectMemberService projectMemberService;

  public List<Role> getRoles(Long projectId, String username) {
    List<ProjectMember> relations =
        projectMemberService.findAllByProjectIdAndUsername(projectId, username);
    List<Role> rols = new ArrayList<>();
    for (ProjectMember relation : relations) {
      rols.add(relation.getUserRole());
    }
    return rols;
  }

  public UserContext getAuthenticatedUser() {
    return (UserContext) (SecurityContextHolder.getContext().getAuthentication().getPrincipal());
  }

  public List<Role> getAuthendicatedUserRoles() {
    Long projectId = getAuthenticatedUser().getProject().getId();
    String username = getAuthenticatedUser().getUsername();
    return getRoles(projectId, username);
  }

  public Project getContextProject(){
    return getAuthenticatedUser().getProject();
  }
}
