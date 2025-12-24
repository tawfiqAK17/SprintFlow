package com.ensa.SprintFlow.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EpicMetaDataResponseDto {
  private Long id;
  private String title;
  private String description;
  private Long userStoriesCount;
}
