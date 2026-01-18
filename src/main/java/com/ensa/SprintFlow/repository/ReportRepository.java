package com.ensa.SprintFlow.repository;

import com.ensa.SprintFlow.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
