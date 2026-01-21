package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.task.request.TaskRequestDto;
import com.ensa.SprintFlow.dto.task.response.TaskDetailsResponseDto;
import com.ensa.SprintFlow.dto.task.response.TaskMetaDataResponseDto;
import com.ensa.SprintFlow.model.Task;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class TaskMapper {
    private UserStoryMapper userStoryMapper;
    private UserMapper userMapper;
    private ReportMapper reportMapper;

    public Task mapToTask(TaskRequestDto taskRequestDto){
        return Task.builder()
                .title( taskRequestDto.getTitle())
                .description( taskRequestDto.getDescription())
                .status( taskRequestDto.getStatus()).build();
    }

    public TaskMetaDataResponseDto mapToTaskMetaDataResponseDto( Task task){
        return TaskMetaDataResponseDto.builder()
                .id( task.getId())
                .title( task.getTitle())
                .description( task.getDescription())
                .status( task.getStatus())
                .userStory( userStoryMapper.mapToUserStoryMetaDataResponseDto( task.getUserStory()))
                .build();
    }

    public List<TaskMetaDataResponseDto> mapToTaskMetaDataResponseDto(List<Task> taskList){
        List<TaskMetaDataResponseDto> taskDtos = new ArrayList<>();
        for( Task task : taskList){
            taskDtos.add( mapToTaskMetaDataResponseDto( task));
        }
        return taskDtos;
    }

    public TaskDetailsResponseDto mapToTaskDetailsResponseDto( Task task){
        return TaskDetailsResponseDto.builder()
                .id( task.getId())
                .title( task.getTitle())
                .description( task.getDescription())
                .status( task.getStatus())
                .userStory( userStoryMapper.mapToUserStoryMetaDataResponseDto( task.getUserStory()))
                .tester( userMapper.mapToMetaDataDto( task.getTester()))
                .developer( userMapper.mapToMetaDataDto( task.getDeveloper()))
                .reports( reportMapper.mapToReportResponseDto( task.getReports()))
                .build();
    }
}
