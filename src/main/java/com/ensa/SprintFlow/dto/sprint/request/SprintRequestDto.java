package com.ensa.SprintFlow.dto.sprint.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class SprintRequestDto {
  @NotBlank(message = "the title is required")
  private String title;

  @NotBlank(message = "the start date is required")
  private LocalDateTime startDate;

  @NotBlank(message = "the end date is required")
  private LocalDateTime endDate;
}
