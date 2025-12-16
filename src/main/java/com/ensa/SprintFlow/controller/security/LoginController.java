package com.ensa.SprintFlow.controller.security;

import com.ensa.SprintFlow.dto.request.LoginRequestDto;
import com.ensa.SprintFlow.dto.response.LoginResponseDto;
import com.ensa.SprintFlow.service.security.LoginService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
  LoginService loginService;

  LoginController(LoginService loginService) {
    this.loginService = loginService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
    LoginResponseDto responseDto = loginService.authenticateUser(dto);
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  @GetMapping("/refresh-token")
  public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
    LoginResponseDto responseDto = loginService.refreshToken(refreshToken);
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }
}
