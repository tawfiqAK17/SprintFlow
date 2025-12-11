package com.ensa.SprintFlow.controller.security;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ensa.SprintFlow.service.security.UserVerificationService;

@RestController
public class UserVerificationController {
  UserVerificationService userVerificationService;
  UserVerificationController(UserVerificationService userVerificationService) {
    this.userVerificationService = userVerificationService;
  }

  @PostMapping("/verify")
  public void verify(@RequestParam String code) {
    userVerificationService.verify(code);
  }
  
}
