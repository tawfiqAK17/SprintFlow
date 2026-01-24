package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.dto.epic.request.EpicRequestDto;
import com.ensa.SprintFlow.dto.epic.response.EpicResponseDto;
import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.mapper.EpicMapper;
import com.ensa.SprintFlow.model.Epic;
import com.ensa.SprintFlow.model.Project;
import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.repository.EpicRepository;
import com.ensa.SprintFlow.security.service.UserAuthorizationService;
import jakarta.transaction.Transactional;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EpicService {

  private EpicRepository epicRepository;
  private EpicMapper mapper;
  private UserAuthorizationService userAuthorizationService;

  public Epic save(Epic epic) {
    return epicRepository.save(epic);
  }

  public List<EpicResponseDto> getAllEpics(Long projectId) {
    return epicRepository.findAllByProjectId(projectId).stream()
        .map(e -> mapper.mapToEpicResponseDto(e))
        .toList();
  }

  public EpicResponseDto getEpic(Long epicId) {
    Optional<Epic> optionalEpic = epicRepository.findById(epicId);
    if (optionalEpic.isEmpty()) {
      throw new NotFoundException("no epic found with the given id");
    }
    return mapper.mapToEpicResponseDto(optionalEpic.get());
  }

  public EpicResponseDto createEpic(Project project, EpicRequestDto dto) {
    Epic epic = epicRepository.save(mapper.mapToEpic(project, dto));
    return mapper.mapToEpicResponseDto(epic);
  }

  public EpicResponseDto update(Long epicId, EpicRequestDto dto) {
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
    return mapper.mapToEpicResponseDto(epic);
  }

  @Transactional
  public void delete(Long epicId) {
    Optional<Epic> optionalEpic = epicRepository.findById(epicId);
    if (optionalEpic.isEmpty()) {
      throw new NotFoundException("there is no epic with the given id");
    }
    Epic epic = optionalEpic.get();

    Epic defaultEpic =
        findById(userAuthorizationService.getContextProject().getDefaultEpic().getId());

    Iterator<UserStory> iterator = epic.getUserStories().iterator();
    while (iterator.hasNext()) {
      UserStory userStory = iterator.next();
      userStory.setEpic(defaultEpic);
      defaultEpic.getUserStories().add(userStory);
      iterator.remove();
    }

    epicRepository.delete(epic);
  }

  @Transactional
  public void addUserStories(Long epicId, List<Long> userStoriesIds) {
    Epic epic =
        epicRepository
            .findById(epicId)
            .orElseThrow(() -> new NotFoundException("there is no epic with the given id"));

    // the project is guaranteed to have a default epic
    Epic defaultEpic =
        findById(userAuthorizationService.getContextProject().getDefaultEpic().getId());

    List<UserStory> userStories =
        epicRepository.findAllUserStoriesByIds(defaultEpic.getId(), userStoriesIds);

    for (UserStory userStory : userStories) {
      defaultEpic.getUserStories().remove(userStory);
      epic.getUserStories().add(userStory);
      userStory.setEpic(epic);
    }
  }

  @Transactional
  public void removeUserStory(Long epicId, Long userStoryId) {
    Optional<Epic> optionalEpic = epicRepository.findById(epicId);
    if (optionalEpic.isEmpty()) {
      throw new NotFoundException("there is no sprint with the given id");
    }
    Epic epic = optionalEpic.get();
    List<UserStory> sprintUserStories = epic.getUserStories();
    sprintUserStories.stream().filter(u -> u.getId() != userStoryId);
  }

  public Epic findById(Long epicId) {
    Optional<Epic> optionalEpic = epicRepository.findById(epicId);
    if (optionalEpic.isEmpty()) {
      throw new NotFoundException("there is no epic with the given name");
    }
    return optionalEpic.get();
  }
}
