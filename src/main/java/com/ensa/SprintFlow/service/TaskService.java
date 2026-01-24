package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.dto.task.request.TaskRequestDto;
import com.ensa.SprintFlow.dto.task.request.TaskUpdateRequestDto;
import com.ensa.SprintFlow.dto.task.response.TaskDetailsResponseDto;
import com.ensa.SprintFlow.dto.task.response.TaskMetaDataResponseDto;
import com.ensa.SprintFlow.enums.Role;
import com.ensa.SprintFlow.enums.TaskStatus;
import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.exception.generalException.UnauthorizedException;
import com.ensa.SprintFlow.mapper.TaskMapper;
import com.ensa.SprintFlow.model.Report;
import com.ensa.SprintFlow.model.Task;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.model.UserStory;
import com.ensa.SprintFlow.repository.TaskRepository;
import com.ensa.SprintFlow.repository.specification.TaskSpecification;
import com.ensa.SprintFlow.security.service.UserAuthorizationService;
import com.ensa.SprintFlow.strategy.userStrategy.UserStrategy;
import com.ensa.SprintFlow.strategy.userStrategy.UserStrategyHandler;

import java.util.List;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TaskService {
  private TaskMapper mapper;
  private UserService userService;
  private UserAuthorizationService userAuthorizationService;
  private UserStoryService userStoryService;
  private TaskRepository taskRepository;
  private UserStrategyHandler userStrategyHandler;
  private TaskTransitionService taskTransitionService;

  public void createTask(Long projectId, Long userStoryId, TaskRequestDto dto) {
    Task task = mapper.mapToTask(dto);

    // check if the user assigned as a developer has this role in the project
    User developer = null;
    if (dto.getDeveloper() != null) {
      String username = dto.getDeveloper();
      if( !userAuthorizationService.hasRoleInProject( projectId, username, Role.DEVELOPER)){
        throw new UnauthorizedException("The given user cannot be assigned as a developer");
      }
      developer = userService.findByUsername( username);
    }
    // check if the user assigned as a Tester has this role in the project
    User tester = null;
    if (dto.getTester() != null) {
      String username = dto.getTester();
      if( !userAuthorizationService.hasRoleInProject( projectId, username, Role.TESTER)){
        throw new UnauthorizedException("The given user cannot be assigned as a tester");
      }
      tester = userService.findByUsername( username);
    }

    UserStory userStory = userStoryService.findUserStory(userStoryId);
    // Check if the userStory belongs to any sprint or not
    if( userStory.getSprint() == null){
      throw new UnauthorizedException("This userStory are not belongs yet to any sprint");
    }

    task.setDeveloper(developer);
    task.setTester(tester);
    task.setUserStory(userStory);

    taskRepository.save(task);
  }

  // dedicated for Scrum Master
  public List<TaskMetaDataResponseDto> getAllTasks(
          Long projectId, Long sprintId, Long userStoryId,
          List<TaskStatus> statusList, String developer, String tester) {

    Specification<Task> spec = TaskSpecification.whereProject( projectId);

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

  // dedicated for other roles: Developer and Tester
  public List<TaskMetaDataResponseDto> getCurrentUserTasks(
          Long projectId, Long sprintId, Long userStoryId,
          List<TaskStatus> statusList) {

    // Get the current user name
    String username = userAuthorizationService.getAuthenticatedUser().getUsername();

    Specification<Task> spec = TaskSpecification.whereProject( projectId);
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
    // Get the current user name and roles
    String username = userAuthorizationService.getAuthenticatedUser().getUsername();
    List<Role> roles = userAuthorizationService.getAuthenticatedUserRoles();

    Task task = findTask( taskId);

    if (!task.getDeveloper().getUsername().equals(username)
        && !task.getTester().getUsername().equals(username)
        && !roles.contains(Role.SCRUM_MASTER)) {
      throw new UnauthorizedException("Unauthorized to access this task");
    }

    TaskDetailsResponseDto test = mapper.mapToTaskDetailsResponseDto(task);
    return mapper.mapToTaskDetailsResponseDto(task);
  }

  @Transactional
  public void updateTaskDetails(Long projectId, Long taskId, TaskRequestDto dto) {
    Task task = findTask( taskId);

    if (dto.getTitle() != null) {
      task.setTitle(dto.getTitle());
    }
    if (dto.getDescription() != null) {
      task.setDescription(dto.getDescription());
    }
    if (dto.getStatus() != null) {
      task.setStatus(dto.getStatus());
    }

    if (dto.getDeveloper() != null) {
      String username = dto.getDeveloper();
      if( !userAuthorizationService.hasRoleInProject( projectId, username, Role.DEVELOPER)){
        throw new UnauthorizedException("The given user cannot be assigned as a developer");
      }
      User developer = userService.findByUsername( username);
      task.setDeveloper( developer);
    }

    if (dto.getTester() != null) {
      String username = dto.getTester();
      if( !userAuthorizationService.hasRoleInProject( projectId, username, Role.TESTER)){
        throw new UnauthorizedException("The given user cannot be assigned as a tester");
      }
      User tester = userService.findByUsername( username);
      task.setTester( tester);
    }
  }

  @Transactional
  public void updateTaskStatus(Long taskId, TaskUpdateRequestDto dto) {
    // Get the task we want to update
    Task task = findTask( taskId);

    // Get the current user name and roles
    String username = userAuthorizationService.getAuthenticatedUser().getUsername();
    List<Role> roles = userAuthorizationService.getAuthenticatedUserRoles();

    // The user must be assigned to the task (as a TESTER and/or DEVELOPER)
    // or have the SCRUM_MASTER role.
    if (!task.getDeveloper().getUsername().equals(username)
            && !task.getTester().getUsername().equals(username)
            && !roles.contains(Role.SCRUM_MASTER)) {
      throw new UnauthorizedException("Unauthorized to access this task");
    }

    // get the appropriate stategy for roles list of the current user
    UserStrategy userStrategy = userStrategyHandler.getStrategy(roles);

    // validate if the current user is allowed to modify the task status
    // and also verify the status transition is it logic
    taskTransitionService.validateTaskStatus( userStrategy, task.getStatus(), dto.getStatus());
    task.setStatus( dto.getStatus());

    // If the task's status is uptated to 'TEST_FAILED', the tester should provide a report
    if (dto.getStatus() == TaskStatus.TEST_FAILED && dto.getReportDescription() != null) {
      Report report = new Report();
      report.setTask(task);
      report.setDescription(dto.getReportDescription());
      task.getReports().add(report);
    }
  }

  public void deleteTask(Long taskId){
    Task task = findTask( taskId);
    taskRepository.delete( task);
  }

  public Task findTask( Long taskId){
    return taskRepository.findById( taskId).orElseThrow(
            () -> new NotFoundException("No task found with the given Id.")
    );
  }
}
