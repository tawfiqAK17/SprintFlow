package com.ensa.SprintFlow.dto.sprint.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SprintRequestDto {

  @NotBlank(message = "the title is required")
  private String title;

  @NotNull(message = "the start date is required")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime startDate;

  @NotNull(message = "the end date is required")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Future(message = "end date must be in the future")
  private LocalDateTime endDate;
}
