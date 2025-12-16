package com.ensa.SprintFlow.controller.security;

import com.ensa.SprintFlow.dto.request.RegisterRequestDto;
import com.ensa.SprintFlow.mapper.RegisterMapper;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.service.security.RegisterService;
import com.ensa.SprintFlow.service.security.UserVerificationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

  RegisterMapper registerMapper;
  RegisterService registerService;
  UserVerificationService userVerificationService;

  public RegisterController(
      RegisterService registerService,
      RegisterMapper registerMapper,
      UserVerificationService userVerificationService) {
    this.registerMapper = registerMapper;
    this.registerService = registerService;
    this.userVerificationService = userVerificationService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> userRegister(@RequestBody RegisterRequestDto dto) {
    User user = registerMapper.mapToEntity(dto);
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
