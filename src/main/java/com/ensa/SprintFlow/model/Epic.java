package com.ensa.SprintFlow.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "epics")
@Getter
@Setter
@NoArgsConstructor
public class Epic {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String description;

  @OneToMany(mappedBy = "epic", cascade = CascadeType.ALL)
  private Set<UserStory> userStories = new HashSet<>();

  // Constructors

  public Epic(String title, String description) {
    this.title = title;
    this.description = description;
  }
}
