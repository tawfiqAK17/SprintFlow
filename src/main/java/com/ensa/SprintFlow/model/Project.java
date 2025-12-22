package com.ensa.SprintFlow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
public class Project {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;

  @Column(nullable = false)
  private LocalDateTime creationDate;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
  private Set<ProjectMember> projectMembers = new HashSet<>();

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
  private Set<ProductBacklog> productBacklogs = new HashSet<>();

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
  private Set<Sprint> sprints = new HashSet<>();

  // Constructors

  public Project(String name, String description) {
    this.name = name;
    this.description = description;
  }
}
