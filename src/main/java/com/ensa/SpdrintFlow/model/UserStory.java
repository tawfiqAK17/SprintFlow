package com.ensa.SpdrintFlow.model;

import jakarta.persistence.*;
import java.util.Set;
import com.ensa.SpdrintFlow.enums.Status;

@Entity
@Table(name = "user_stories")
public class UserStory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userStoryId;
    
    private String title;
    private String description;
    private Integer priority;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    @ManyToOne
    @JoinColumn(name = "product_backlog_id")
    private ProductBacklog productBacklog;
    
    @OneToMany(mappedBy = "userStory")
    private Set<Task> tasks;
    
    @ManyToMany
    @JoinTable(
        name = "user_story_epics",
        joinColumns = @JoinColumn(name = "user_story_id"),
        inverseJoinColumns = @JoinColumn(name = "epic_id")
    )
    private Set<Epic> epics;
    
    // Getters and Setters
    public Long getUserStoryId() { return userStoryId; }
    public void setUserStoryId(Long userStoryId) { this.userStoryId = userStoryId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    
    public ProductBacklog getProductBacklog() { return productBacklog; }
    public void setProductBacklog(ProductBacklog productBacklog) { this.productBacklog = productBacklog; }
    
    public Set<Task> getTasks() { return tasks; }
    public void setTasks(Set<Task> tasks) { this.tasks = tasks; }
    
    public Set<Epic> getEpics() { return epics; }
    public void setEpics(Set<Epic> epics) { this.epics = epics; }
}
