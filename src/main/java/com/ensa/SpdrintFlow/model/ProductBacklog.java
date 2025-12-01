package com.ensa.SpdrintFlow.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "product_backlogs")
public class ProductBacklog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long backlogId;
    
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    
    @OneToMany(mappedBy = "productBacklog")
    private Set<SprintBacklog> sprintBacklogs;
    
    @OneToMany(mappedBy = "productBacklog")
    private Set<UserStory> userStories;
    
    // Getters and Setters
    public Long getBacklogId() { return backlogId; }
    public void setBacklogId(Long backlogId) { this.backlogId = backlogId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    
    public Set<SprintBacklog> getSprintBacklogs() { return sprintBacklogs; }
    public void setSprintBacklogs(Set<SprintBacklog> sprintBacklogs) { this.sprintBacklogs = sprintBacklogs; }
    
    public Set<UserStory> getUserStories() { return userStories; }
    public void setUserStories(Set<UserStory> userStories) { this.userStories = userStories; }
}
