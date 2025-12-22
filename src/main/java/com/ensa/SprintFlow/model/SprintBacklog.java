package com.ensa.SprintFlow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sprint_backlogs")
@Getter
@Setter
@NoArgsConstructor
public class SprintBacklog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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

  public SprintBacklog(String name, Sprint sprint, UserStory userStory) {
    this.name = name;
    this.sprint = sprint;
    this.userStory = userStory;
  }
}
