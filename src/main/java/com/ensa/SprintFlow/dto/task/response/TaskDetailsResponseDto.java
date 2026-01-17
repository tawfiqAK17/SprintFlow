package com.ensa.SprintFlow.dto.task.response;

import com.ensa.SprintFlow.dto.report.response.ReportResponseDto;
import com.ensa.SprintFlow.dto.user.response.UserMetaDataResponseDto;
import com.ensa.SprintFlow.dto.userStory.response.UserStoryMetaDataResponseDto;
import com.ensa.SprintFlow.enums.TaskStatus;
import com.ensa.SprintFlow.model.Report;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
