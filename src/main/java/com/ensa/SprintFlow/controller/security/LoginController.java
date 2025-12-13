package com.ensa.SprintFlow.controller.security;

import com.ensa.SprintFlow.dto.request.LoginRequestDto;
import com.ensa.SprintFlow.dto.response.LoginResponseDto;
import com.ensa.SprintFlow.service.security.LoginService;
import com.ensa.SprintFlow.service.security.UserVerificationService;
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
  UserVerificationService userVerificationService;

  LoginController(LoginService loginService, UserVerificationService userVerificationService) {
    this.loginService = loginService;
    this.userVerificationService = userVerificationService;
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
    LoginResponseDto responseDto = loginService.authenticateUser(dto);
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }

  @PostMapping("/verify")
  public ResponseEntity<?> verify(@RequestParam String code) {
    userVerificationService.verify(code);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/verify/resend")
  public ResponseEntity<?> resendVerificationCode(@RequestParam String username) {
    userVerificationService.resendVerificationCode(username);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/refresh-token")
  public ResponseEntity<?> refreshToken(@RequestParam String refreshToken) {
    LoginResponseDto responseDto = loginService.refreshToken(refreshToken);
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }
}
