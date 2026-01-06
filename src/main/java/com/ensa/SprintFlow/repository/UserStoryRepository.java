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
            e.id,
            e.title,
            s.id,
            s.title
        )
        FROM UserStory u
        LEFT JOIN u.epic e 
        LEFT JOIN u.sprint s
        WHERE (:epicId IS NULL OR e.id = :epicId) 
             AND (((:sprintId IS NULL OR s.id = :sprintId) AND :unassigned = FALSE) 
                 OR ( s.id IS NULL AND :unassigned = TRUE)) 
    """)
    List<UserStoryView> findAllUserStories(Long epicId, Long sprintId, Boolean unassigned);


    @Query("""
            SELECT u FROM UserStory u 
            LEFT JOIN FETCH u.epic 
            LEFT JOIN FETCH u.sprint 
            LEFT JOIN FETCH u.userStoryDescription 
            LEFT JOIN FETCH u.acceptanceCriteria
            WHERE u.id = :id
           """)
    UserStory findUserStory(@Param("id") Long UserStoryId);
}
