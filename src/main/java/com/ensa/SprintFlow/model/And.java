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

import java.util.Objects;

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

  @Override
  public int hashCode() {
    return Objects.hash(andWhat);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    And and = (And) o;
    return Objects.equals(andWhat, and.andWhat);
  }
}
// The original schema of ands table: (we changed it as this word is reserved)
/*
  String and
*/