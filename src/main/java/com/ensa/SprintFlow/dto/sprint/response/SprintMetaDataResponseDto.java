package com.ensa.SprintFlow.dto.sprint.response;

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
public class SprintMetaDataResponseDto {
  private Long id;
  private String title;
}
