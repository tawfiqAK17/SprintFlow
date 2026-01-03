package com.ensa.SprintFlow.security.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class RegisterRequestDto {

  @NotBlank(message = "first name is required")
  private String firstName;

  @NotBlank(message = "last name is required")
  private String lastName;

  @NotBlank(message = "username is required")
  private String username;

  @NotBlank(message = "email is required")
  private String email;

  @NotBlank(message = "password is required")
  private String password;
}
