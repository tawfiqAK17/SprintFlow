package com.ensa.SprintFlow.dto.epic.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EpicMetaDataResponseDto {
  private Long id;
  private String title;
}
