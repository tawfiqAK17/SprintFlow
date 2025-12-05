package com.ensa.SprintFlow.model;

import jakarta.persistence.*;

@Entity
@Table(name = "acceptance_criteria")
public class AcceptanceCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long acceptanceCriteriaId;
    
    private String given;
    
    @Column(name = "when_condition")
    private String when;
    
    @Column(name = "then_condition")
    private Integer then;
    
    @OneToOne
    @JoinColumn(name = "sprint_backlog_id")
    private SprintBacklog sprintBacklog;
    
    // Constructors
    public AcceptanceCriteria() {}
    
    public AcceptanceCriteria(String given, String when, Integer then) {
        this.given = given;
        this.when = when;
        this.then = then;
    }
    
    // Getters and Setters
    public Long getAcceptanceCriteriaId() {
        return acceptanceCriteriaId;
    }
    
    public void setAcceptanceCriteriaId(Long acceptanceCriteriaId) {
        this.acceptanceCriteriaId = acceptanceCriteriaId;
    }
    
    public String getGiven() {
        return given;
    }
    
    public void setGiven(String given) {
        this.given = given;
    }
    
    public String getWhen() {
        return when;
    }
    
    public void setWhen(String when) {
        this.when = when;
    }
    
    public Integer getThen() {
        return then;
    }
    
    public void setThen(Integer then) {
        this.then = then;
    }
    
    public SprintBacklog getSprintBacklog() {
        return sprintBacklog;
    }
    
    public void setSprintBacklog(SprintBacklog sprintBacklog) {
        this.sprintBacklog = sprintBacklog;
    }
}
