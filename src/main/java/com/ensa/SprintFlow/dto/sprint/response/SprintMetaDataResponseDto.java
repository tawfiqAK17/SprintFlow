package com.ensa.SprintFlow.dto.sprint.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class SprintMetaDataResponseDto {
  private Long id;
  private String title;
}
