package com.ensa.SprintFlow.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class EpicCreationRequestDto {
  @NotBlank(message = "title is required")
  private String title;

  @NotBlank(message = "description is required")
  private String description;
}
