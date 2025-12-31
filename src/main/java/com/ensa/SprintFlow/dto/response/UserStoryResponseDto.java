package com.ensa.SprintFlow.dto.response;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserStoryResponseDto {
  private Long id;
  private String title;
  private Integer priority;
  private SprintMetaDateResponseDto sprint;
}
