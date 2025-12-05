package com.ensa.SprintFlow.model;

import jakarta.persistence.*;

import com.ensa.SprintFlow.enums.Role;

@Entity
@Table(name = "project_members")
public class ProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectMemberId;
    
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Enumerated(EnumType.STRING)
    private Role userRole;
    
    // Constructors
    public ProjectMember() {}
    
    public ProjectMember(Project project, User user, Role userRole) {
        this.project = project;
        this.user = user;
        this.userRole = userRole;
    }
    
    // Getters and Setters
    public Long getProjectMemberId() {
        return projectMemberId;
    }
    
    public void setProjectMemberId(Long projectMemberId) {
        this.projectMemberId = projectMemberId;
    }
    
    public Project getProject() {
        return project;
    }
    
    public void setProject(Project project) {
        this.project = project;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Role getUserRole() {
        return userRole;
    }
    
    public void setUserRole(Role userRole) {
        this.userRole = userRole;
    }
}
