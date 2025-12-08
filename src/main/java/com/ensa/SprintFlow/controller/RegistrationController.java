package com.ensa.SprintFlow.controller;

import com.ensa.SprintFlow.dto.UserDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/register")
public class RegistrationController {

  @PostMapping
  public void userRegister(@Validated @RequestBody UserDto userDto) {}
}
