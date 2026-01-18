package com.ensa.SprintFlow.dto.epic.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
public class EpicMetaDataResponseDto {
  private Long id;
  private String title;
}
