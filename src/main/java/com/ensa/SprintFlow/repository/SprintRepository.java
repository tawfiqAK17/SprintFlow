package com.ensa.SprintFlow.repository;

import com.ensa.SprintFlow.model.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {}
