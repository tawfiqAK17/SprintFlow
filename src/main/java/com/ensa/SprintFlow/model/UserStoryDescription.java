package com.ensa.SprintFlow.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "user_story_descriptions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserStoryDescription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String asWho;

  @Column(nullable = false)
  private String what;

  @Column(nullable = false)
  private String forDesc;

}
