package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.dto.sprint.request.SprintRequestDto;
import com.ensa.SprintFlow.dto.sprint.response.SprintBurndownChartDto;
import com.ensa.SprintFlow.dto.sprint.response.SprintResponseDto;
import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.mapper.SprintMapper;
import com.ensa.SprintFlow.model.Sprint;
import com.ensa.SprintFlow.model.Task;
import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.repository.SprintRepository;
import com.ensa.SprintFlow.security.service.UserAuthorizationService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
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
  UserAuthorizationService userAuthorizationService;

  public SprintResponseDto save(SprintRequestDto dto) {
    Sprint sprint = sprintMapper.mapToSprint(dto);
    sprint.setProject(userAuthorizationService.getAuthenticatedUser().getProject());
    return sprintMapper.maptoSprintResponseDto(sprintRepository.save(sprint));
  }

  public List<SprintResponseDto> getAllSprints(
      Long projectId, LocalDateTime startDate, LocalDateTime endDate) {
    List<Sprint> sprints = sprintRepository.findAllByProjectId(projectId);

    if (startDate != null) {
      sprints = sprints.stream().filter(s -> s.getStartDate().isAfter(startDate)).toList();
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
    sprintUserStories.removeIf(u -> u.getId() != userStoryId);
  }

  public void delete(Long sprintId) {
    sprintRepository.deleteById(sprintId);
  }

  public Sprint findById(Long sprintId) {
    Optional<Sprint> optionalSprint = sprintRepository.findById(sprintId);
    if (optionalSprint.isEmpty()) {
      throw new NotFoundException("there is no sprint with the given id");
    }
    return optionalSprint.get();
  }

  public SprintBurndownChartDto getBurndownChart(Long sprintId) {
    // Fetch the sprint by ID
    Sprint sprint =
        sprintRepository
            .findById(sprintId)
            .orElseThrow(() -> new NotFoundException("there is no sprint with the given id"));

    // Get all tasks for this sprint
    List<Task> tasks = sprintRepository.findAllTasks(sprintId);

    // Calculate total number of tasks
    Integer numberOfTasks = tasks.size();

    // Get sprint start and end dates
    LocalDateTime sprintStartDate = sprint.getStartDate();
    LocalDateTime sprintEndDate = sprint.getEndDate();

    // Calculate number of days in the sprint
    Integer numberOfDays =
        (int) ChronoUnit.DAYS.between(sprintStartDate.toLocalDate(), sprintEndDate.toLocalDate())
            + 1; // +1 to include both start and end days

    // Initialize list to store tasks done per day
    List<Integer> numberOfTasksDoneForDay = new ArrayList<>(Collections.nCopies(numberOfDays, 0));

    // Count tasks completed on each day
    for (Task task : tasks) {
      if (task.getDoneDate() != null) {
        LocalDateTime doneDate = task.getDoneDate();

        // Check if task was completed within sprint period
        if (!doneDate.isBefore(sprintStartDate) && !doneDate.isAfter(sprintEndDate)) {
          // Calculate which day of the sprint this task was completed
          int dayIndex =
              (int) ChronoUnit.DAYS.between(sprintStartDate.toLocalDate(), doneDate.toLocalDate());

          // Increment the count for that day
          if (dayIndex >= 0 && dayIndex < numberOfDays) {
            numberOfTasksDoneForDay.set(dayIndex, numberOfTasksDoneForDay.get(dayIndex) + 1);
          }
        }
      }
    }

    // Build and return the DTO
    return SprintBurndownChartDto.builder()
        .sprintStartDate(sprintStartDate)
        .numberOfTasks(numberOfTasks)
        .numberOfDays(numberOfDays)
        .numberOfTasksDoneForDay(numberOfTasksDoneForDay)
        .build();
  }
}
