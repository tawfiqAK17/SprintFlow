package com.ensa.SprintFlow.model.security;

import com.ensa.SprintFlow.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
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

  public Long getRefreshTokenId() {
    return refreshTokenId;
  }

  public void setRefreshTokenId(Long refreshTokenId) {
    this.refreshTokenId = refreshTokenId;
  }

  public LocalDateTime getExpirationDate() {
    return expirationDate;
  }

  public void setExpirationDate(LocalDateTime expirationDate) {
    this.expirationDate = expirationDate;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
