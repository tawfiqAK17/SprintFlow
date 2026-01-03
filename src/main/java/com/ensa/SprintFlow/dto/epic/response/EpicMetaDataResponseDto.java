package com.ensa.SprintFlow.dto.epic.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class EpicMetaDataResponseDto {
  private Long id;
  private String title;
}
