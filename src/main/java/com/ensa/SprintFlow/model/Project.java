package com.ensa.SprintFlow.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;
    
    private String name;
    private String description;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<ProjectMember> projectMembers = new HashSet<>();
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<ProductBacklog> productBacklogs = new HashSet<>();
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private Set<Sprint> sprints = new HashSet<>();
    
    // Constructors
    public Project() {}
    
    public Project(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    // Getters and Setters
    public Long getProjectId() {
        return projectId;
    }
    
    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Set<ProjectMember> getProjectMembers() {
        return projectMembers;
    }
    
    public void setProjectMembers(Set<ProjectMember> projectMembers) {
        this.projectMembers = projectMembers;
    }
    
    public Set<ProductBacklog> getProductBacklogs() {
        return productBacklogs;
    }
    
    public void setProductBacklogs(Set<ProductBacklog> productBacklogs) {
        this.productBacklogs = productBacklogs;
    }
    
    public Set<Sprint> getSprints() {
        return sprints;
    }
    
    public void setSprints(Set<Sprint> sprints) {
        this.sprints = sprints;
    }
}
