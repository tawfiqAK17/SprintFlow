package com.ensa.SprintFlow.controller.security;

import com.ensa.SprintFlow.dto.request.RegisterRequestDto;
import com.ensa.SprintFlow.mapper.RegisterMapper;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.service.security.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

  RegisterMapper registerMapper;
  RegisterService registerService;

  public RegisterController(RegisterService registerService, RegisterMapper registerMapper) {
    this.registerMapper = registerMapper;
    this.registerService = registerService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> userRegister(@RequestBody RegisterRequestDto dto) {
    User user = registerMapper.mapToEntity(dto);
    registerService.register(user);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
