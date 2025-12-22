package com.ensa.SprintFlow.model;

import com.ensa.SprintFlow.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "project_members")
@Getter
@Setter
@NoArgsConstructor
public class ProjectMember {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project project;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Enumerated(EnumType.STRING)
  private Role userRole;

  // Constructors

  public ProjectMember(Project project, User user, Role userRole) {
    this.project = project;
    this.user = user;
    this.userRole = userRole;
  }
}
