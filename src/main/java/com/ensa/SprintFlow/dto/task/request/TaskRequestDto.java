package com.ensa.SprintFlow.dto.task.request;

import com.ensa.SprintFlow.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TaskRequestDto {
    @NotBlank(message = "title is required")
    private String title;
    private String description;
    private TaskStatus status;
    private String developer;
    private String tester;
}
