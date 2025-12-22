package com.ensa.SprintFlow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private LocalDateTime enrollDate;

  @Column(nullable = false)
  private boolean verified;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<ProjectMember> projectMembers = new HashSet<>();

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private Set<UserStory> userStories = new HashSet<>();

  // Constructors

  public User(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }
}
