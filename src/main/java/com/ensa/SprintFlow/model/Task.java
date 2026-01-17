package com.ensa.SprintFlow.model;

import java.util.List;

import com.ensa.SprintFlow.enums.TaskStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Enumerated(EnumType.STRING)
  private TaskStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="tester_id")
  private User tester;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name ="developer_id")
  private User developer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_story_id")
  private UserStory userStory;

  @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
  private List<Report> reports;
}
