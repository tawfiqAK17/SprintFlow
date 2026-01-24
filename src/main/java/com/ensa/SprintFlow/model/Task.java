package com.ensa.SprintFlow.model;

import com.ensa.SprintFlow.enums.TaskStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String description;

  @Enumerated(EnumType.STRING)
  private TaskStatus status;

  @Setter(AccessLevel.NONE)
  private LocalDateTime doneDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tester_id")
  private User tester;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "developer_id")
  private User developer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_story_id")
  private UserStory userStory;

  @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
  private List<Report> reports;

  @PreUpdate
  @PrePersist
  public void setDoneDateIfCompleted() {
    if (status == TaskStatus.DONE && doneDate == null) {
      doneDate = LocalDateTime.now();
    }
  }
}
