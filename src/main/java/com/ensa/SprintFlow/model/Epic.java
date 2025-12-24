package com.ensa.SprintFlow.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "epics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Epic {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @ManyToOne
  @JoinColumn(name = "user_story_id")
  private UserStory userStory;
}
