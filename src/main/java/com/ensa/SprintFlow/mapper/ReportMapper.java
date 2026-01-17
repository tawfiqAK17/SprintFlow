package com.ensa.SprintFlow.mapper;

import com.ensa.SprintFlow.dto.report.response.ReportResponseDto;
import com.ensa.SprintFlow.model.Report;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReportMapper {
    public ReportResponseDto mapToReportResponseDto(Report report){
        return ReportResponseDto.builder()
                .id( report.getId())
                .description( report.getDescription())
                .creationDate( report.getCreationDate())
                .build();
    }

    public List<ReportResponseDto> mapToReportResponseDto( List<Report> reports){
        List<ReportResponseDto> reportResponseDtoList = new ArrayList<>();
        for( Report report : reports){
            reportResponseDtoList.add( mapToReportResponseDto( report));
        }
        return reportResponseDtoList;
    }
}
