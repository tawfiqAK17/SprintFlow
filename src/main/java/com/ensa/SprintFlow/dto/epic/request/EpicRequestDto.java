package com.ensa.SprintFlow.dto.epic.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EpicRequestDto {
  @NotBlank(message = "title is required")
  private String title;

  @NotBlank(message = "description is required")
  private String description;
}
