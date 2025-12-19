package com.ensa.SprintFlow.service.security;

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

  public List<Role> getRoles(Long projectId, Long userId) {
    List<ProjectMember> relations =
        projectMemberService.findAllByUserIdAndProjectId(userId, projectId);
    List<Role> rols = new ArrayList<>();
    for (ProjectMember relation : relations) {
      rols.add(relation.getUserRole());
    }
    return rols;
  }
}
