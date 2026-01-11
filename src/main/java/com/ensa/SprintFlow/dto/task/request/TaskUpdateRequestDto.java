package com.ensa.SprintFlow.dto.task.request;

import com.ensa.SprintFlow.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskUpdateRequestDto {
//    @NotBlank( message = "Status is required")
    TaskStatus status;
    String reportDescription;
}
