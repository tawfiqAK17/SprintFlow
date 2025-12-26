package com.ensa.SprintFlow.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class EpicUpdateRequestDto {
  private String title;
  private String description;
}
