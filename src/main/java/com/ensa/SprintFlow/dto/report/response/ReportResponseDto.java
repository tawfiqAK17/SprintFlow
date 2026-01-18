package com.ensa.SprintFlow.dto.report.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ReportResponseDto {
  private Long id;
  private String description;
  private LocalDateTime creationDate;
}
