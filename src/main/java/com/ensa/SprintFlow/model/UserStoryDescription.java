package com.ensa.SprintFlow.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_story_descriptions")
@Getter
@Setter
@NoArgsConstructor
public class UserStoryDescription {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "as_description")
  private String as;

  private String what;

  @Column(name = "for_description")
  private String forDescription;

  @OneToOne
  @JoinColumn(name = "user_story_id")
  private UserStory userStory;

  // Constructors

  public UserStoryDescription(String as, String what, String forDescription) {
    this.as = as;
    this.what = what;
    this.forDescription = forDescription;
  }
}
