package com.ensa.SprintFlow.model;

import jakarta.persistence.*;

@Entity
@Table(name = "sprint_backlogs")
public class SprintBacklog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long backlogId;
    
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "sprint_id")
    private Sprint sprint;
    
    @ManyToOne
    @JoinColumn(name = "user_story_id")
    private UserStory userStory;
    
    @OneToOne(mappedBy = "sprintBacklog", cascade = CascadeType.ALL)
    private AcceptanceCriteria acceptanceCriteria;
    
    // Constructors
    public SprintBacklog() {}
    
    public SprintBacklog(String name, Sprint sprint, UserStory userStory) {
        this.name = name;
        this.sprint = sprint;
        this.userStory = userStory;
    }
    
    // Getters and Setters
    public Long getBacklogId() {
        return backlogId;
    }
    
    public void setBacklogId(Long backlogId) {
        this.backlogId = backlogId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Sprint getSprint() {
        return sprint;
    }
    
    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }
    
    public UserStory getUserStory() {
        return userStory;
    }
    
    public void setUserStory(UserStory userStory) {
        this.userStory = userStory;
    }
    
    public AcceptanceCriteria getAcceptanceCriteria() {
        return acceptanceCriteria;
    }
    
    public void setAcceptanceCriteria(AcceptanceCriteria acceptanceCriteria) {
        this.acceptanceCriteria = acceptanceCriteria;
    }
}
