package com.ensa.SprintFlow.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  @OneToMany(mappedBy = "acceptanceCriteria", cascade = CascadeType.ALL)
  private List<And> ands;
}
