package com.ensa.SprintFlow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "acceptance_criteria")
@Getter
@Setter
@NoArgsConstructor
public class AcceptanceCriteria {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String given;

  @Column(name = "when_condition")
  private String when;

  @Column(name = "then_condition")
  private Integer then;

  @OneToOne
  @JoinColumn(name = "sprint_backlog_id")
  private SprintBacklog sprintBacklog;

  // Constructors
  public AcceptanceCriteria(String given, String when, Integer then) {
    this.given = given;
    this.when = when;
    this.then = then;
  }
}
