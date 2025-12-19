package com.ensa.SprintFlow.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "sprints")
@Getter
@Setter
@NoArgsConstructor
public class Sprint {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private LocalDate startDate;
  private LocalDate endDate;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project project;

  @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL)
  private Set<SprintBacklog> sprintBacklogs = new HashSet<>();

  // Constructors

  public Sprint(LocalDate startDate, LocalDate endDate, Project project) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.project = project;
  }
}
