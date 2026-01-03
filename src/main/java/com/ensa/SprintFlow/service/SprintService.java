package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.dto.sprint.request.SprintRequestDto;
import com.ensa.SprintFlow.dto.sprint.response.SprintResponseDto;
import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.mapper.SprintMapper;
import com.ensa.SprintFlow.model.Sprint;
import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.repository.SprintRepository;
import com.ensa.SprintFlow.util.Utils;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SprintService {
  SprintRepository sprintRepository;
  SprintMapper sprintMapper;
  UserStoryService userStoryService;

  public SprintResponseDto save(SprintRequestDto dto) {
    Sprint sprint = sprintMapper.mapToSprint(dto);
    sprint.setProject(Utils.getUserContext().getProject());
    return sprintMapper.maptoSprintResponseDto(sprintRepository.save(sprint));
  }

  public List<SprintResponseDto> getAllSprints(
      LocalDateTime startDate, LocalDateTime endDate, Boolean active) {
    List<Sprint> sprints = Utils.getUserContext().getProject().getSprints();
    if (active != null) {
      // TODO : the isActive property should be added to the sprint model
    }

    if (startDate != null) {
      sprints = sprints.stream().filter(s -> s.getStartDate().isBefore(startDate)).toList();
    }
    if (endDate != null) {
      sprints = sprints.stream().filter(s -> s.getStartDate().isBefore(endDate)).toList();
    }

    return sprints.stream().map(s -> sprintMapper.maptoSprintResponseDto(s)).toList();
  }

  public SprintResponseDto getSprint(Long sprintId) {
    Optional<Sprint> optionalSprint = sprintRepository.findById(sprintId);
    if (optionalSprint.isEmpty()) {
      throw new NotFoundException("the is no sprint with the given id");
    }
    Sprint sprint = optionalSprint.get();
    return sprintMapper.maptoSprintResponseDto(sprint);
  }

  public SprintResponseDto update(Long sprintId, SprintRequestDto dto) {
    // the sprint is guarantied to exist due to the resource hierarchy validation filter
    Optional<Sprint> optionalSprint = sprintRepository.findById(sprintId);
    if (optionalSprint.isEmpty()) {
      throw new NotFoundException("the is no sprint with the given id");
    }
    Sprint sprint = optionalSprint.get();
    if (dto.getTitle() != null) {
      sprint.setTitle(dto.getTitle());
    }
    if (dto.getStartDate() != null) {
      sprint.setStartDate(dto.getStartDate());
    }
    if (dto.getEndDate() != null) {
      sprint.setEndDate(dto.getEndDate());
    }
    return sprintMapper.maptoSprintResponseDto(sprintRepository.save(sprint));
  }

  @Transactional
  public void addUserStories(Long sprintId, List<Long> userStoriesIds) {
    Optional<Sprint> optionalSprint = sprintRepository.findById(sprintId);
    if (optionalSprint.isEmpty()) {
      throw new NotFoundException("there is no sprint with the given id");
    }
    Sprint sprint = optionalSprint.get();
    List<UserStory> sprintUserStories = sprint.getUserStories();
    List<UserStory> userStories = userStoryService.findAllByIds(userStoriesIds);
    for (UserStory userStory : userStories) {
      sprintUserStories.add(userStory);
    }
  }

  @Transactional
  public void removeUserStory(Long sprintId, Long userStoryId) {
    Optional<Sprint> optionalSprint = sprintRepository.findById(sprintId);
    if (optionalSprint.isEmpty()) {
      throw new NotFoundException("there is no sprint with the given id");
    }
    Sprint sprint = optionalSprint.get();
    List<UserStory> sprintUserStories = sprint.getUserStories();
    sprintUserStories.stream().filter(u -> u.getId() != userStoryId);
  }
}
