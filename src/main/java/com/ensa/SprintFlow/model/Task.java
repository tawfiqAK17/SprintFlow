package com.ensa.SprintFlow.model;

import com.ensa.SprintFlow.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
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

  public Task(String title, String description, Status status) {
    this.title = title;
    this.description = description;
    this.status = status;
  }
}
