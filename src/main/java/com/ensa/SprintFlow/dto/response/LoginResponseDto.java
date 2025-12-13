package com.ensa.SprintFlow.dto.response;

public class LoginResponseDto {

  private String jwt;
  private String refreshToken;

  public LoginResponseDto(String jwt, String refreshToken) {
    this.jwt = jwt;
    this.refreshToken = refreshToken;
  }

  public String getRefreshToken() {
    return refreshToken;
  }

  public void setRefreshToken(String refreshToken) {
    this.refreshToken = refreshToken;
  }

  public String getJwt() {
    return jwt;
  }

  public void setJwt(String jwt) {
    this.jwt = jwt;
  }
}
