package com.ensa.SprintFlow.dto.task.request;

import com.ensa.SprintFlow.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskUpdateRequestDto {
  @NotNull( message = "Status is required")
  TaskStatus status;
  String reportDescription;
}
