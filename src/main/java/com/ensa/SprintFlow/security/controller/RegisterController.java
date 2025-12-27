package com.ensa.SprintFlow.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ensa.SprintFlow.dto.request.RegisterRequestDto;
import com.ensa.SprintFlow.mapper.RegisterMapper;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.security.service.RegisterService;
import com.ensa.SprintFlow.security.service.UserVerificationService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class RegisterController {

  RegisterMapper registerMapper;
  RegisterService registerService;
  UserVerificationService userVerificationService;

  @PostMapping("/register")
  public ResponseEntity<?> userRegister(@Validated @RequestBody RegisterRequestDto dto) {
    User user = registerMapper.mapToUser(dto);
    registerService.register(user);
    return ResponseEntity.status(HttpStatus.CREATED).build();
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
}
