package com.ensa.SprintFlow.repository;

import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.model.ProjectMember;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

  ProjectMember findByProjectAndUserRole(Project project, Role role);

  List<ProjectMember> findAllByUserIdAndProjectId(Long userId, Long projectId);

  void deleteByProjectIdAndUserRole(Long projectId, Role scrumMaster);
}
