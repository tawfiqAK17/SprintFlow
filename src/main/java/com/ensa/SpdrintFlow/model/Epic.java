package com.ensa.SpdrintFlow.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "epics")
public class Epic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long epicId;
    
    private String title;
    private String description;
    
    @ManyToMany(mappedBy = "epics")
    private Set<UserStory> userStories;
    
    // Getters and Setters
    public Long getEpicId() { return epicId; }
    public void setEpicId(Long epicId) { this.epicId = epicId; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Set<UserStory> getUserStories() { return userStories; }
    public void setUserStories(Set<UserStory> userStories) { this.userStories = userStories; }
}
