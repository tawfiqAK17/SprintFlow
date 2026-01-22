package com.ensa.SprintFlow.repository;

import com.ensa.SprintFlow.model.UserStory;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, Long>, JpaSpecificationExecutor<UserStory> {

  @EntityGraph( attributePaths = {"epic", "sprint"})
  List<UserStory> findAll(Specification spec);

  @Query(
      """
       SELECT u FROM UserStory u
       LEFT JOIN FETCH u.epic
       LEFT JOIN FETCH u.sprint
       LEFT JOIN FETCH u.userStoryDescription
       WHERE u.id = :id
      """)
  UserStory findUserStory(@Param("id") Long UserStoryId);

  @Query(
      """
      SELECT u FROM UserStory u WHERE u.id IN :ids
      """)
  List<UserStory> findAllByIds(@Param("ids") List<Long> ids);
}
