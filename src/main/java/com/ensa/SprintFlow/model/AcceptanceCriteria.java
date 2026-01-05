package com.ensa.SprintFlow.model;

import jakarta.persistence.*;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.repository.cdi.Eager;

@Entity
@Table(name = "acceptance_criteria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcceptanceCriteria {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String _given;

  @Column(nullable = false)
  private String _when;

  @Column(nullable = false)
  private String _then;

  @ManyToOne
  @JoinColumn(name = "user_story_id")
  private UserStory userStory;

  @OneToMany(mappedBy = "acceptanceCriteria", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<And> ands;
}
