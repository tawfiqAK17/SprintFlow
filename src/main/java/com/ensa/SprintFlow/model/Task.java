package com.ensa.SprintFlow.model;

import com.ensa.SprintFlow.enums.Status;
import jakarta.persistence.*;

@Entity
@Table(name = "tasks")
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String description;

  @Enumerated(EnumType.STRING)
  private Status status;

  @ManyToOne
  @JoinColumn(name = "user_story_id")
  private UserStory userStory;

  // Constructors
  public Task() {}

  public Task(String title, String description, Status status) {
    this.title = title;
    this.description = description;
    this.status = status;
  }

  // Getters and Setters

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public UserStory getUserStory() {
    return userStory;
  }

  public void setUserStory(UserStory userStory) {
    this.userStory = userStory;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
