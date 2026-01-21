package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.report.response.ReportResponseDto;
import com.ensa.SprintFlow.model.Report;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReportMapper {

    ReportResponseDto mapToReportResponseDto(Report report);

    List<ReportResponseDto> mapToReportResponseDto(List<Report> reports);
}
