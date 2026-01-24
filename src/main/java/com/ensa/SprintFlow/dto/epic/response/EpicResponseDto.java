package com.ensa.SprintFlow.dto.epic.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EpicResponseDto {
  private Long id;
  private String title;
  private String description;
  private int userStoriesCount;
}
