package com.ensa.SprintFlow.dto.sprint.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class SprintBurndownChartDto {
  private LocalDateTime sprintStartDate;
  private Integer numberOfTasks;
  private Integer numberOfDays;
  List<Integer> numberOfTasksDoneForDay;
}
