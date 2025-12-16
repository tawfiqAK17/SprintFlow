package com.ensa.SprintFlow.model;

import com.ensa.SprintFlow.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "project_members")
public class ProjectMember {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
