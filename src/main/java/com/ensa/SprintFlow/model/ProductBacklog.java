package com.ensa.SprintFlow.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_backlogs")
@Getter
@Setter
@NoArgsConstructor
public class ProductBacklog {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project project;

  @OneToMany(mappedBy = "productBacklog", cascade = CascadeType.ALL)
  private Set<UserStory> userStories = new HashSet<>();

  // Constructors

  public ProductBacklog(String name, Project project) {
    this.name = name;
    this.project = project;
  }
}
