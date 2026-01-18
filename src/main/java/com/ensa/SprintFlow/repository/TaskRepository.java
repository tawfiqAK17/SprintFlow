package com.ensa.SprintFlow.repository;

import com.ensa.SprintFlow.model.Task;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> , JpaSpecificationExecutor<Task> {
    @Override
    @EntityGraph(attributePaths = {"userStory"})
    List<Task> findAll(Specification<Task> spec);

    @Override
    @EntityGraph( attributePaths = {"userStory", "tester", "developer", "reports"} )
    Optional<Task> findById(Long taskId);
}
