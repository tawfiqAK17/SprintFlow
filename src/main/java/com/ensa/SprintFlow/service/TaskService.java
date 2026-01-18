package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.dto.task.request.TaskRequestDto;
import com.ensa.SprintFlow.dto.task.request.TaskUpdateRequestDto;
import com.ensa.SprintFlow.dto.task.response.TaskDetailsResponseDto;
import com.ensa.SprintFlow.dto.task.response.TaskMetaDataResponseDto;
import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.enums.TaskStatus;
import com.ensa.SprintFlow.exception.generalException.UnauthorizedException;
import com.ensa.SprintFlow.mapper.TaskMapper;
import com.ensa.SprintFlow.model.Report;
import com.ensa.SprintFlow.model.Task;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.repository.TaskRepository;
import com.ensa.SprintFlow.repository.specification.TaskSpecification;
import com.ensa.SprintFlow.security.model.UserContext;
import com.ensa.SprintFlow.service.strategy.TaskTransitionStrategy;
import com.ensa.SprintFlow.service.strategy.TaskTransitionStrategyHandler;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TaskService {
  private TaskMapper mapper;
  private UserService userService;
  private UserStoryService userStoryService;
  private TaskRepository taskRepository;
  private TaskTransitionStrategyHandler taskTransitionStrategyHandler;

  public void createTask(Long userStoryId, TaskRequestDto dto) {
    Task task = mapper.mapToTaskRequestDto(dto);
    System.out.println(dto.getStatus());
    User developer = null;
    if (dto.getDeveloper() != null) {
      developer = userService.findByUsername(dto.getDeveloper());
    }
    User tester = null;
    if (dto.getTester() != null) {
      tester = userService.findByUsername(dto.getTester());
    }
    UserStory userStory = userStoryService.findUserStory(userStoryId);

    task.setDeveloper(developer);
    task.setTester(tester);
    task.setUserStory(userStory);

    taskRepository.save(task);
  }

  // customized for Scrum Master
  public List<TaskMetaDataResponseDto> getAllTasks(
      Long sprintId,
      Long userStoryId,
      List<TaskStatus> statusList,
      String developer,
      String tester) {

    Specification<Task> spec = TaskSpecification.emptyWhere();

    if (sprintId != null) {
      spec = spec.and(TaskSpecification.belongsToSprint(sprintId));
    }
    if (userStoryId != null) {
      spec = spec.and(TaskSpecification.belongsToUserStory(userStoryId));
    }
    if (statusList != null && !statusList.isEmpty()) {
      spec = spec.and(TaskSpecification.hasStatus(statusList));
    }
    if (developer != null) {
      spec = spec.and(TaskSpecification.assignedToDeveloper(developer));
    }
    if (tester != null) {
      spec = spec.and(TaskSpecification.assignedToTester(tester));
    }

    List<Task> tasks = taskRepository.findAll(spec);
    return mapper.mapToTaskMetaDataResponseDto(tasks);
  }

  // customized for other roles: Developer and Tester
  public List<TaskMetaDataResponseDto> getCurrentUserTasks(
      Long sprintId, Long userStoryId, List<TaskStatus> statusList) {
    // Get the current user name
    UserContext userContext =
        (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username = userContext.getUsername();

    Specification<Task> spec = TaskSpecification.emptyWhere();
    // Assigned to the current user either as a Tester or as a Developer
    spec =
        spec.and(
            TaskSpecification.assignedToTester(username)
                .or(TaskSpecification.assignedToDeveloper(username)));
    // Other parameters to consider if they are provided
    if (sprintId != null) {
      spec = spec.and(TaskSpecification.belongsToSprint(sprintId));
    }
    if (userStoryId != null) {
      spec = spec.and(TaskSpecification.belongsToUserStory(userStoryId));
    }
    if (statusList != null && !statusList.isEmpty()) {
      spec = spec.and(TaskSpecification.hasStatus(statusList));
    }

    List<Task> tasks = taskRepository.findAll(spec);

    return mapper.mapToTaskMetaDataResponseDto(tasks);
  }

  public TaskDetailsResponseDto getTask(Long taskId) {
    // Get the current user name
    UserContext userContext =
        (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username = userContext.getUsername();
    // Get the current user roles
    // List<Role> roles = userContext.getRoles();
    List<Role> roles = List.of(Role.TESTER);

    Task task = taskRepository.findById(taskId).orElseThrow();

    if (!task.getDeveloper().getUsername().equals(username)
        && !task.getTester().getUsername().equals(username)
        && !roles.contains(Role.SCRUM_MASTER)) {
      throw new UnauthorizedException("You can't access this task");
    }
    TaskDetailsResponseDto test = mapper.mapToTaskDetailsResponseDto(task);
    return mapper.mapToTaskDetailsResponseDto(task);
  }
  ;

  @Transactional
  public void updateTaskDetails(Long taskId, TaskRequestDto dto) {
    TaskTransitionStrategy strategy =
        taskTransitionStrategyHandler.getStrategy(List.of(Role.SCRUM_MASTER));

    Task task = taskRepository.findById(taskId).orElseThrow();

    if (dto.getTitle() != null) {
      task.setTitle(dto.getTitle());
    }
    if (dto.getDescription() != null) {
      task.setDescription(dto.getDescription());
    }
    if (dto.getStatus() != null) {
      strategy.validateStatus(task.getStatus(), dto.getStatus());
      task.setStatus(dto.getStatus());
    }
    if (dto.getDeveloper() != null) {
      User developer = userService.findByUsername(dto.getDeveloper());
      task.setDeveloper(developer);
    }
    if (dto.getTester() != null) {
      User tester = userService.findByUsername(dto.getTester());
      task.setTester(tester);
    }
  }

  @Transactional
  public void updateTaskStatus(Long taskId, TaskUpdateRequestDto dto) {
    // Get the task we want to update
    Task task = taskRepository.findById(taskId).orElseThrow();

    // Get the current user name and roles
    UserContext userContext =
            (UserContext) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    String username = userContext.getUsername();
    // List<Role> roles = userContext.getRoles();
    List<Role> roles = List.of(Role.TESTER);

    // The user must be assigned to the task (as a TESTER and/or DEVELOPER)
    // or have the SCRUM_MASTER role.
    if (!task.getDeveloper().getUsername().equals(username)
            && !task.getTester().getUsername().equals(username)
            && !roles.contains(Role.SCRUM_MASTER)) {
      throw new UnauthorizedException("You can't access this task");
    }

    // get the appropriate stategy for roles of current user
    TaskTransitionStrategy strategy = taskTransitionStrategyHandler.getStrategy(roles);

    // validate if the current user roles allows him to modify the task status
    // and also verify the status transition is it logic
    strategy.validateStatus(task.getStatus(), dto.getStatus());
    task.setStatus(dto.getStatus());

    // If the task's status is uptated to 'TEST_FAILED', the tester should provide a report
    if (dto.getStatus() == TaskStatus.TEST_FAILED && dto.getReportDescription() != null) {
      Report report = new Report();
      report.setTask(task);
      report.setDescription(dto.getReportDescription());
      task.getReports().add(report);
    }
  }

  public void deleteTask(Long taskId){
      taskRepository.deleteById( taskId);
  }

}
