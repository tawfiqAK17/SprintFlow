package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.dto.request.EpicCreationRequestDto;
import com.ensa.SprintFlow.dto.request.EpicUpdateRequestDto;
import com.ensa.SprintFlow.dto.response.EpicMetaDataResponseDto;
import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.mapper.EpicMapper;
import com.ensa.SprintFlow.model.Epic;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.repository.EpicRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EpicService {

  private EpicRepository epicRepository;
  private EpicMapper mapper;

  public Epic save(Epic epic) {
    return epicRepository.save(epic);
  }

  public List<Epic> getAllEpics(Long projectId) {
    return epicRepository.findAllByProjectId(projectId);
  }

  public Epic getEpic(Long epicId) {
    Optional<Epic> optionalEpic = epicRepository.findById(epicId);
    if (optionalEpic.isEmpty()) {
      throw new NotFoundException("no epic found with the given id");
    }
    return optionalEpic.get();
  }

  public EpicMetaDataResponseDto createEpic(Project project, EpicCreationRequestDto dto) {
    Epic epic = epicRepository.save(mapper.mapToEpic(project, dto));
    return mapper.mapToEpicMetaDataResponseDto(epic);
  }

  public EpicMetaDataResponseDto update(Long epicId, EpicUpdateRequestDto dto) {
    Optional<Epic> optionalEpic = epicRepository.findById(epicId);
    if (optionalEpic.isEmpty()) {
      throw new NotFoundException("there is no epic with the given id");
    }
    Epic epic = optionalEpic.get();
    if (dto.getTitle() != null) {
      epic.setTitle(dto.getTitle());
    }
    if (dto.getDescription() != null) {
      epic.setDescription(dto.getDescription());
    }
    epic = epicRepository.save(epic);
    return mapper.mapToEpicMetaDataResponseDto(epic);
  }

  public void delete(Long epicId) {
    epicRepository.deleteById(epicId);
  }
}
