package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.task.request.TaskRequestDto;
import com.ensa.SprintFlow.dto.task.response.TaskDetailsResponseDto;
import com.ensa.SprintFlow.dto.task.response.TaskMetaDataResponseDto;
import com.ensa.SprintFlow.model.Task;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = {UserStoryMapper.class, UserMapper.class, ReportMapper.class})
public interface TaskMapper {
  @Mapping(target = "tester", ignore = true)
  @Mapping(target = "developer", ignore = true)
  Task mapToTaskRequestDto(TaskRequestDto taskRequestDto);

  TaskMetaDataResponseDto mapToTaskMetaDataResponseDto(Task task);

  List<TaskMetaDataResponseDto> mapToTaskMetaDataResponseDto(List<Task> taskList);

  TaskDetailsResponseDto mapToTaskDetailsResponseDto(Task task);
}
