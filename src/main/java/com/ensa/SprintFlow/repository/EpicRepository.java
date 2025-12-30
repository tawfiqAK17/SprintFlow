package com.ensa.SprintFlow.repository;

import com.ensa.SprintFlow.model.Epic;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EpicRepository extends JpaRepository<Epic, Long> {
  List<Epic> findAllByProjectId(Long projectId);
}
