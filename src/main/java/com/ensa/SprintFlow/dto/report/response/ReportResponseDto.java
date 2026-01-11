package com.ensa.SprintFlow.dto.report.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ReportResponseDto {
    private Long id;
    private String description;
    private LocalDateTime creationDate;
}
