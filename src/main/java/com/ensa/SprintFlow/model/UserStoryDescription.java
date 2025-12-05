package com.ensa.SprintFlow.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_story_descriptions")
public class UserStoryDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userStoryDescriptionId;
    
    @Column(name = "as_description")
    private String as;
    
    private String what;
    
    @Column(name = "for_description")
    private String forDescription;
    
    @OneToOne
    @JoinColumn(name = "user_story_id")
    private UserStory userStory;
    
    // Constructors
    public UserStoryDescription() {}
    
    public UserStoryDescription(String as, String what, String forDescription) {
        this.as = as;
        this.what = what;
        this.forDescription = forDescription;
    }
    
    // Getters and Setters
    public Long getUserStoryDescriptionId() {
        return userStoryDescriptionId;
    }
    
    public void setUserStoryDescriptionId(Long userStoryDescriptionId) {
        this.userStoryDescriptionId = userStoryDescriptionId;
    }
    
    public String getAs() {
        return as;
    }
    
    public void setAs(String as) {
        this.as = as;
    }
    
    public String getWhat() {
        return what;
    }
    
    public void setWhat(String what) {
        this.what = what;
    }
    
    public String getForDescription() {
        return forDescription;
    }
    
    public void setForDescription(String forDescription) {
        this.forDescription = forDescription;
    }
    
    public UserStory getUserStory() {
        return userStory;
    }
    
    public void setUserStory(UserStory userStory) {
        this.userStory = userStory;
    }
}
