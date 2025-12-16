package com.ensa.SprintFlow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "users")
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
  public User() {}

  public User(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  // Getters and Setters

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Set<ProjectMember> getProjectMembers() {
    return projectMembers;
  }

  public void setProjectMembers(Set<ProjectMember> projectMembers) {
    this.projectMembers = projectMembers;
  }

  public Set<UserStory> getUserStories() {
    return userStories;
  }

  public void setUserStories(Set<UserStory> userStories) {
    this.userStories = userStories;
  }

  public String getUserName() {
    return username;
  }

  public void setUserName(String userName) {
    this.username = userName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public boolean isVerified() {
    return verified;
  }

  public void setVerified(boolean verified) {
    this.verified = verified;
  }

  public LocalDateTime getEnrollDate() {
    return enrollDate;
  }

  public void setEnrollDate(LocalDateTime enrollDate) {
    this.enrollDate = enrollDate;
  }

  public Long getId() {
	return id;
  }

  public void setId(Long id) {
	this.id = id;
  }
}
