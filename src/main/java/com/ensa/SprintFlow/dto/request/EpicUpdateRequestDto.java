package com.ensa.SprintFlow.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EpicUpdateRequestDto {
  private String title;
  private String description;
}
