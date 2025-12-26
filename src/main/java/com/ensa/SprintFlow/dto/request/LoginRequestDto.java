package com.ensa.SprintFlow.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class LoginRequestDto {
  @NotBlank(message = "username is required")
  private String username;

  @NotBlank(message = "password is required")
  private String password;
}
