package com.ensa.SprintFlow.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_stories")
public class UserStory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userStoryId;
    
    private String title;
    private Integer priority;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "product_backlog_id")
    private ProductBacklog productBacklog;
    
    @ManyToOne
    @JoinColumn(name = "epic_id")
    private Epic epic;
    
    @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL)
    private Set<Task> tasks = new HashSet<>();
    
    @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL)
    private Set<SprintBacklog> sprintBacklogs = new HashSet<>();
    
    @OneToOne(mappedBy = "userStory", cascade = CascadeType.ALL)
    private UserStoryDescription userStoryDescription;
    
    // Constructors
    public UserStory() {}
    
    public UserStory(String title, Integer priority) {
        this.title = title;
        this.priority = priority;
    }
    
    // Getters and Setters
    public Long getUserStoryId() {
        return userStoryId;
    }
    
    public void setUserStoryId(Long userStoryId) {
        this.userStoryId = userStoryId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public Integer getPriority() {
        return priority;
    }
    
    public void setPriority(Integer priority) {
        this.priority = priority;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public ProductBacklog getProductBacklog() {
        return productBacklog;
    }
    
    public void setProductBacklog(ProductBacklog productBacklog) {
        this.productBacklog = productBacklog;
    }
    
    public Epic getEpic() {
        return epic;
    }
    
    public void setEpic(Epic epic) {
        this.epic = epic;
    }
    
    public Set<Task> getTasks() {
        return tasks;
    }
    
    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }
    
    public Set<SprintBacklog> getSprintBacklogs() {
        return sprintBacklogs;
    }
    
    public void setSprintBacklogs(Set<SprintBacklog> sprintBacklogs) {
        this.sprintBacklogs = sprintBacklogs;
    }
    
    public UserStoryDescription getUserStoryDescription() {
        return userStoryDescription;
    }
    
    public void setUserStoryDescription(UserStoryDescription userStoryDescription) {
        this.userStoryDescription = userStoryDescription;
    }
}
