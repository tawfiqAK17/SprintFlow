package com.ensa.SprintFlow.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_stories")
@Getter
@Setter
@NoArgsConstructor
public class UserStory {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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

  public UserStory(String title, Integer priority) {
    this.title = title;
    this.priority = priority;
  }
}
