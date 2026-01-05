package com.ensa.SprintFlow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class And {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String andWhat;

  @ManyToOne
  @JoinColumn(name = "acceptance_criteria_id")
  private AcceptanceCriteria acceptanceCriteria;
}
// The original schema of ands table: (we changed it as this word is reserved)
/*
  String and
*/