package com.ensa.SprintFlow.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sprints")
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sprintId;
    
    private LocalDate startDate;
    private LocalDate endDate;
    
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    
    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL)
    private Set<SprintBacklog> sprintBacklogs = new HashSet<>();
    
    // Constructors
    public Sprint() {}
    
    public Sprint(LocalDate startDate, LocalDate endDate, Project project) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.project = project;
    }
    
    // Getters and Setters
    public Long getSprintId() {
        return sprintId;
    }
    
    public void setSprintId(Long sprintId) {
        this.sprintId = sprintId;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public Project getProject() {
        return project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    
    public Set<SprintBacklog> getSprintBacklogs() {
        return sprintBacklogs;
    }
    
    public void setSprintBacklogs(Set<SprintBacklog> sprintBacklogs) {
        this.sprintBacklogs = sprintBacklogs;
    }
}
