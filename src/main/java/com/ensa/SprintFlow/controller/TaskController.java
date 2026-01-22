package com.ensa.SprintFlow.controller;

import com.ensa.SprintFlow.dto.task.request.TaskRequestDto;
import com.ensa.SprintFlow.dto.task.request.TaskUpdateRequestDto;
import com.ensa.SprintFlow.dto.task.response.TaskDetailsResponseDto;
import com.ensa.SprintFlow.dto.task.response.TaskMetaDataResponseDto;
import com.ensa.SprintFlow.enums.TaskStatus;
import com.ensa.SprintFlow.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@AllArgsConstructor
public class TaskController {
    private TaskService taskService;

    @PostMapping("/projects/{project_id}/user_stories/{user_story_id}/tasks")
    public ResponseEntity<?> createTask(@PathVariable("project_id") Long projectId,
                                        @PathVariable("user_story_id") Long userStoryId,
                                        @Validated @RequestBody TaskRequestDto taskRequestDto){

        taskService.createTask( projectId ,userStoryId, taskRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/projects/{project_id}/tasks")
    public ResponseEntity<?> getAllTasks(@PathVariable("project_id") Long projectId,
                                         @RequestParam(required = false) Long sprintId,
                                         @RequestParam(required = false) Long userStoryId,
                                         @RequestParam(required = false) List<TaskStatus> status,
                                         @RequestParam(required = false) String developer,
                                         @RequestParam(required = false) String tester){

        List<TaskMetaDataResponseDto> taskList = taskService.getAllTasks( projectId, sprintId, userStoryId, status, developer, tester);
        return ResponseEntity.status(HttpStatus.OK).body( taskList);
    }

    @GetMapping("/projects/{project_id}/tasks/me")
    public ResponseEntity<?> getCurrentUserTasks(@PathVariable("project_id") Long projectId,
                                                 @RequestParam(required = false) Long sprintId,
                                                 @RequestParam(required = false) Long userStoryId,
                                                 @RequestParam(required = false) List<TaskStatus> status){

        List<TaskMetaDataResponseDto> taskList = taskService.getCurrentUserTasks(projectId, sprintId, userStoryId, status);
        return ResponseEntity.status(HttpStatus.OK).body( taskList);
    }

    @GetMapping("/projects/{project_id}/user_stories/{user_story_id}/tasks/{task_id}")
    public ResponseEntity<?> getTask(@PathVariable("project_id") Long projectId,
                                                @PathVariable("user_story_id") Long userStoryId,
                                                @PathVariable("task_id") Long taskId){
        TaskDetailsResponseDto taskResponseDto = taskService.getTask( taskId);
        return ResponseEntity.status( HttpStatus.OK).body( taskResponseDto);
    }

    @PutMapping("/projects/{project_id}/user_stories/{user_story_id}/tasks/{task_id}")
    public ResponseEntity<?> updateTaskDetails(@PathVariable("project_id") Long projectId,
                                               @PathVariable("user_story_id") Long userStoryId,
                                               @PathVariable("task_id") Long taskId,
                                               @RequestBody TaskRequestDto taskRequestDto){
        taskService.updateTaskDetails( projectId ,taskId, taskRequestDto);
        return ResponseEntity.status( HttpStatus.OK).build();
    }

    @PutMapping("/projects/{project_id}/user_stories/{user_story_id}/tasks/{task_id}/status")
    public ResponseEntity<?> updateTaskStatus(@PathVariable("project_id") Long projectId,
                                               @PathVariable("user_story_id") Long userStoryId,
                                               @PathVariable("task_id") Long taskId,
                                               @Validated @RequestBody TaskUpdateRequestDto taskUpdateRequestDto){
        taskService.updateTaskStatus( taskId, taskUpdateRequestDto);
        return ResponseEntity.status( HttpStatus.OK).build();
    }

    @DeleteMapping("/projects/{project_id}/user_stories/{user_story_id}/tasks/{task_id}")
    public ResponseEntity<?> deleteTask(@PathVariable("project_id") Long projectId,
                                        @PathVariable("user_story_id") Long userStoryId,
                                        @PathVariable("task_id") Long taskId){
        taskService.deleteTask( taskId);
        return ResponseEntity.status( HttpStatus.NO_CONTENT).build();
    }
}
