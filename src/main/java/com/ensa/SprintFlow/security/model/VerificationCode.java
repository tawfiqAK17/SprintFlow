package com.ensa.SprintFlow.security.model;

import java.time.LocalDateTime;

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

@Entity
@Getter
@Setter
@Table(name = "verification_codes")
public class VerificationCode {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long verificationCodeId;

  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private LocalDateTime expirationDate;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
}
