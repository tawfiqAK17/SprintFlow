package com.ensa.SprintFlow.security.service;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.model.ProjectMember;
import com.ensa.SprintFlow.service.ProjectMemberService;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
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
}
