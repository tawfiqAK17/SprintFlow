package com.ensa.SprintFlow.repository;

import com.ensa.SprintFlow.model.Sprint;
import com.ensa.SprintFlow.model.Task;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {

  @Query(
      """
      SELECT t
      FROM Task t
      WHERE t.userStory.sprint.id = :sprintId
      """)
  List<Task> findAllTasks(@Param("sprintId") Long sprintId);

  List<Sprint> findAllByProjectId(Long projectId);
}
