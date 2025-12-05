package com.ensa.SprintFlow.model;

import jakarta.persistence.*;

import com.ensa.SprintFlow.enums.Status;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    
    private String title;
    private String description;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @ManyToOne
    @JoinColumn(name = "user_story_id")
    private UserStory userStory;
    
    // Constructors
    public Task() {}
    
    public Task(String title, String description, Status status) {
        this.title = title;
        this.description = description;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getTaskId() {
        return taskId;
    }
    
    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public UserStory getUserStory() {
        return userStory;
    }
    
    public void setUserStory(UserStory userStory) {
        this.userStory = userStory;
    }
}
