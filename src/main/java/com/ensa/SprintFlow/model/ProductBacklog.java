package com.ensa.SprintFlow.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_backlogs")
public class ProductBacklog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project project;

  @OneToMany(mappedBy = "productBacklog", cascade = CascadeType.ALL)
  private Set<UserStory> userStories = new HashSet<>();

  // Constructors
  public ProductBacklog() {}

  public ProductBacklog(String name, Project project) {
    this.name = name;
    this.project = project;
  }

  // Getters and Setters

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  public Set<UserStory> getUserStories() {
    return userStories;
  }

  public void setUserStories(Set<UserStory> userStories) {
    this.userStories = userStories;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
