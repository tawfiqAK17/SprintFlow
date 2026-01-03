package com.ensa.SprintFlow.dto.sprint.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class SprintResponseDto {
  private Long id;
  private String title;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  // private boolean isActive;
  private Integer userStoriesCount;
}
