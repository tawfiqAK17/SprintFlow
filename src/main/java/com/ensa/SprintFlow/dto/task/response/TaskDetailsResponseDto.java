package com.ensa.SprintFlow.dto.task.response;

import com.ensa.SprintFlow.dto.report.response.ReportResponseDto;
import com.ensa.SprintFlow.dto.user.response.UserMetaDataResponseDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryMetaDataResponseDto;
import com.ensa.SprintFlow.enums.TaskStatus;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TaskDetailsResponseDto {
  private Long id;
  private String title;
  private String description;
  private TaskStatus status;
  private UserStoryMetaDataResponseDto userStory;
  private UserMetaDataResponseDto developer;
  private UserMetaDataResponseDto tester;
  private List<ReportResponseDto> reports;
}
