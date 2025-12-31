package com.ensa.SprintFlow.security.model;

import com.ensa.SprintFlow.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long refreshTokenId;

  @Column(nullable = false)
  private String token;

  @Column(nullable = false)
  private LocalDateTime expirationDate;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
