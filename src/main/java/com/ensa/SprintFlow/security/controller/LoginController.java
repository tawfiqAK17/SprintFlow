package com.ensa.SprintFlow.security.controller;

import com.ensa.SprintFlow.builder.CustomResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ensa.SprintFlow.security.dto.request.LoginRequestDto;
import com.ensa.SprintFlow.security.dto.response.LoginResponseDto;
import com.ensa.SprintFlow.security.service.LoginService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class LoginController {
  LoginService loginService;
  CustomResponseBuilder responseBuilder;

  @PostMapping("/login")
  public ResponseEntity<?> login(@Validated @RequestBody LoginRequestDto dto) {
    LoginResponseDto responseDto = loginService.authenticateUser(dto);
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  @GetMapping("/refresh-token")
  public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
    String newJwtToken = loginService.refreshToken(refreshToken);
    return responseBuilder.status(HttpStatus.OK).property("jwt", newJwtToken).build();
  }
}
