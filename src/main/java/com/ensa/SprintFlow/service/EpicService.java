package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.model.Epic;
import com.ensa.SprintFlow.repository.EpicRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EpicService {
  private EpicRepository epicRepository;

  public Epic save(Epic epic) {
    return epicRepository.save(epic);
  }
}
