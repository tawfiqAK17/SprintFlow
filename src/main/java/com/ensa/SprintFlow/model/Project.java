package com.ensa.SprintFlow.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private LocalDateTime creationDate;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
  private List<ProjectMember> projectMembers;

  @OneToOne
  @JoinColumn(name = "default_epic_id")
  private Epic defaultEpic;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
  private List<Epic> epics;

  @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
  private List<Sprint> sprints;
}
