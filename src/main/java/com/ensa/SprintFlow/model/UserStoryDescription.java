package com.ensa.SprintFlow.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_story_descriptions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStoryDescription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String as;

  @Column(nullable = false)
  private String what;

  @Column(nullable = false)
  private String forDesc;

  @ManyToOne
  @JoinColumn(name = "user_story_id")
  private UserStory userStory;

  @OneToMany(mappedBy = "userStoryDescription", cascade = CascadeType.ALL)
  private List<AcceptanceCriteria> acceptanceCriteria;
}
