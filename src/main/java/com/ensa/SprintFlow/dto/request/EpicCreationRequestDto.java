package com.ensa.SprintFlow.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class EpicCreationRequestDto {
  @NotBlank(message = "title is required")
  private String title;

  @NotBlank(message = "description is required")
  private String description;
}
