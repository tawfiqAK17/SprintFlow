package com.ensa.SprintFlow.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class SprintMetaDateResponseDto {
  private Long id;
  private String title;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
}
