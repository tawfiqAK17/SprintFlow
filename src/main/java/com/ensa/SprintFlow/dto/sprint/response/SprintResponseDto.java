package com.ensa.SprintFlow.dto.sprint.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SprintResponseDto {
  private Long id;
  private String title;
  private LocalDateTime startDate;
  private LocalDateTime endDate;
  private Integer userStoriesCount;
}
