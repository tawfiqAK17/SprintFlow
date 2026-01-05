package com.ensa.SprintFlow.repository;

import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.repository.projection.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStoryRepository extends JpaRepository<UserStory, Long> {
    @Query("""
        SELECT new com.ensa.SprintFlow.repository.projection.UserStoryView(
            u.id,
            u.title,
            u.priority,
            new com.ensa.SprintFlow.repository.projection.EpicView(e.id, e.title),
            new com.ensa.SprintFlow.repository.projection.SprintView(s.id, s.title)
        )
        FROM UserStory u
        LEFT JOIN u.epic e 
        LEFT JOIN u.sprint s
        WHERE (:epic_id IS NULL OR e.id = :epic_id) AND (:sprint_id IS NULL OR s.id = :sprint_id)
    """)
    List<UserStoryView> findAllUserStories(Long epic_id, Long sprint_id);


    @Query("""
            SELECT u FROM UserStory u 
            LEFT JOIN FETCH u.epic 
            LEFT JOIN FETCH u.sprint 
            LEFT JOIN FETCH u.userStoryDescription 
            WHERE u.id = :id
           """)
    UserStory findUserStory(@Param("id") Long UserStoryId);
}
