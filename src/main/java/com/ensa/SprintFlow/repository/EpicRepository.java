package com.ensa.SprintFlow.repository;

import com.ensa.SprintFlow.model.Epic;
import com.ensa.SprintFlow.model.UserStory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EpicRepository extends JpaRepository<Epic, Long> {

  List<Epic> findAllByProjectId(Long projectId);

  @Query(
      """
      SELECT us FROM Epic e
      JOIN e.userStories us
      WHERE e.id = :epicId AND us.id IN :userStoryIds
      """)
  List<UserStory> findAllUserStoriesByIds(
      @Param("epicId") Long epicId, @Param("userStoryIds") List<Long> userStoryIds);
}
