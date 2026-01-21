package com.ensa.SprintFlow.model;

import jakarta.persistence.*;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "user_stories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicUpdate
public class UserStory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  @Embedded
  private UserStoryMetrics metrics;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "project_id")
  private Project project;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sprint_id")
  private Sprint sprint;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "epic_id")
  private Epic epic;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "userStoryDescriptionId")
  private UserStoryDescription userStoryDescription;

  @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL)
  private List<AcceptanceCriteria> acceptanceCriteria;

  @OneToMany(mappedBy = "userStory", cascade = CascadeType.ALL)
  private List<Task> tasks;
}
