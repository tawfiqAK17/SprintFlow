package com.ensa.SpdrintFlow.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    
    private String firstName;
    private String lastName;
    
    @OneToMany(mappedBy = "user")
    private Set<ProjectMember> projectMembers;
    
    @OneToMany(mappedBy = "assignedUser")
    private Set<Task> tasks;
    
    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public Set<ProjectMember> getProjectMembers() { return projectMembers; }
    public void setProjectMembers(Set<ProjectMember> projectMembers) { this.projectMembers = projectMembers; }
    
    public Set<Task> getTasks() { return tasks; }
    public void setTasks(Set<Task> tasks) { this.tasks = tasks; }
}
