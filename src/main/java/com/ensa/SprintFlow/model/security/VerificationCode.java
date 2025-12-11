package com.ensa.SprintFlow.model.security;

import com.ensa.SprintFlow.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "verification_codes")
public class VerificationCode {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long verificationCodeId;

  @Column(nullable = false)
  private String code;

  @Column(nullable = false)
  private LocalDateTime expirationDate;

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  public Long getVerificationCodeId() {
    return verificationCodeId;
  }

  public void setVerificationCodeId(Long verificationCodeId) {
    this.verificationCodeId = verificationCodeId;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public LocalDateTime getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(LocalDateTime expirationDate) {
    this.expirationDate = expirationDate;
  }
}
