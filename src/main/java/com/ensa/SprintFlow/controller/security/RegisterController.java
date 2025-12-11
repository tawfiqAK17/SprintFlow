package com.ensa.SprintFlow.controller.security;

import com.ensa.SprintFlow.dto.request.RegisterRequestDto;
import com.ensa.SprintFlow.exception.registrationException.RegistrationException;
import com.ensa.SprintFlow.exception.registrationException.userCredentialsConditionsExeption.EmailConditionsException;
import com.ensa.SprintFlow.exception.registrationException.userCredentialsConditionsExeption.PasswordConditionsException;
import com.ensa.SprintFlow.exception.registrationException.userCredentialsConditionsExeption.UserCredentialsConditionsExeption;
import com.ensa.SprintFlow.exception.registrationException.userDataIntegrityExeption.UserDataIntegrityExeption;
import com.ensa.SprintFlow.mapper.RegisterMapper;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.service.security.RegisterService;
import java.util.ArrayList;
import java.util.HashMap;
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
    try {
      registerService.register(user);
    } catch (RegistrationException e) {
      return handleRegistrationExeption(e);
    }
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  private ResponseEntity<?> handleRegistrationExeption(RegistrationException e) {
    if (e instanceof UserDataIntegrityExeption) {
      return handleUserDataIntegrityException(e);
    }
    if (e instanceof UserCredentialsConditionsExeption) {
      return handleUserCredentialsConditionsExeption(e);
    }
    return null;
  }

  private ResponseEntity<?> handleUserDataIntegrityException(RegistrationException e) {
    return null;
  }

  private ResponseEntity<?> handleUserCredentialsConditionsExeption(RegistrationException e) {
    HashMap<String, Object> body = new HashMap<>();
    ArrayList<String> details = new ArrayList<>();
    if (e instanceof PasswordConditionsException) {
      details = getPasswordConditionsExceptionResponseDetails();
    }
    if (e instanceof EmailConditionsException) {
      details = getEmailConditionsExceptionResponseDetails();
    }
    body.put("error", "CONFLICT");
    body.put("message", e.getMessage());
    body.put("status", HttpStatus.CONFLICT.value());
    body.put("details", details);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
  }

  private ArrayList<String> getEmailConditionsExceptionResponseDetails() {
    return null;
  }

  private ArrayList<String> getPasswordConditionsExceptionResponseDetails() {
    return null;
  }
}
