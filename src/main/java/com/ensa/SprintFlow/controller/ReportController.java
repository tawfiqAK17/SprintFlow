package com.ensa.SprintFlow.controller;

import com.ensa.SprintFlow.service.ReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ReportController {
    private ReportService reportService;

    @DeleteMapping("/projects/{project_id}/tasks/{task_id}/reports/{report_id}")
    public ResponseEntity<?> deleteReport(@PathVariable("project_id") Long projectId,
                                          @PathVariable("task_id") Long taskId,
                                          @PathVariable("report_id") Long reportId){
        reportService.deleteReport( reportId);
        return ResponseEntity.status( HttpStatus.NO_CONTENT).build();
    }
}
