package com.ensa.SprintFlow.controller;

import com.ensa.SprintFlow.dto.request.RegisterRequestDto;
import com.ensa.SprintFlow.mapper.RegisterMapper;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.service.security.RegisterService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

  RegisterMapper registerMapper;
  RegisterService registerService;

  public RegisterController(RegisterService registerService,RegisterMapper registerMapper) {
    this.registerMapper = registerMapper;
    this.registerService = registerService;
  }

  @PostMapping("/register")
  public void userRegister(@RequestBody RegisterRequestDto dto) {
    User user = registerMapper.mapToEntity(dto);
    registerService.register(user);
  }
}
