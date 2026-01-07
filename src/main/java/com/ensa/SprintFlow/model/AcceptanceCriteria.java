package com.ensa.SprintFlow.model;

import jakarta.persistence.*;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "acceptance_criteria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcceptanceCriteria {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String givenWhat;

  @Column(nullable = false)
  private String whenWhat;

  @Column(nullable = false)
  private String thenWhat;

  @ManyToOne
  @JoinColumn(name = "user_story_id")
  private UserStory userStory;

  @OneToMany(mappedBy = "acceptanceCriteria", cascade = CascadeType.ALL, orphanRemoval = true , fetch = FetchType.EAGER)
  private Set<And> ands;
}
// The original schema of acceptance_criteria table: (we changed it as these words are reserved)
/*
  String when
  String when
  String then
*/



