package com.ensa.SprintFlow.controller.security;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.ensa.SprintFlow.builder.ResponseBuilder;
import com.ensa.SprintFlow.dto.request.LoginRequestDto;
import com.ensa.SprintFlow.dto.response.LoginResponseDto;
import com.ensa.SprintFlow.security.service.LoginService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class LoginController {
  LoginService loginService;
  ResponseBuilder responseBuilder;

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
