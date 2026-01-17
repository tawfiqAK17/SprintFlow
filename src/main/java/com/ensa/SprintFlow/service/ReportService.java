package com.ensa.SprintFlow.service;

import com.ensa.SprintFlow.exception.generalException.NotFoundException;
import com.ensa.SprintFlow.model.Report;
import com.ensa.SprintFlow.repository.ReportRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class ReportService {
    private ReportRepository reportRepository;

    public void deleteReport(Long id){
        Report report = findReport( id);
        reportRepository.delete( report);
    }

    public Report findReport( Long id) {
        return reportRepository.findById( id).orElseThrow(
                () -> new NotFoundException("There is no report with this Id"));
    }
}
