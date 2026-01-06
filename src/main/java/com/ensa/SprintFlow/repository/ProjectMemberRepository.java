package com.ensa.SprintFlow.repository;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.model.ProjectMember;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

  ProjectMember findByProjectIdAndUserRole(Long projectId, Role role);

  List<ProjectMember> findAllByProjectIdAndUserRole(Long projectId, Role role);

  void deleteByProjectIdAndUserRole(Long projectId, Role scrumMaster);

  List<ProjectMember> findAllByProjectId(Long projectId);

  List<ProjectMember> findAllByProjectIdAndUserUsername(Long projectId, String username);
}
