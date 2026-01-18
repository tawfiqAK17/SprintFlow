package com.ensa.SprintFlow.dto.epic.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EpicResponseDto {
  private Long id;
  private String title;
  private String description;
  private int userStoriesCount;
}
