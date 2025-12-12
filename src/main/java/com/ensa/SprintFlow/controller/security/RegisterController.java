package com.ensa.SprintFlow.controller.security;

import com.ensa.SprintFlow.builder.ErrorResponseBuilder;
import com.ensa.SprintFlow.dto.request.RegisterRequestDto;
import com.ensa.SprintFlow.exception.registrationException.RegistrationException;
import com.ensa.SprintFlow.exception.registrationException.userCredentialsConditionsException.EmailConditionsException;
import com.ensa.SprintFlow.exception.registrationException.userCredentialsConditionsException.PasswordConditionsException;
import com.ensa.SprintFlow.exception.registrationException.userCredentialsConditionsException.UserCredentialsConditionsException;
import com.ensa.SprintFlow.exception.registrationException.userDataIntegrityException.UserDataIntegrityException;
import com.ensa.SprintFlow.mapper.RegisterMapper;
import com.ensa.SprintFlow.model.User;
import com.ensa.SprintFlow.service.security.RegisterService;
import java.util.ArrayList;
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
      return handleRegistrationException(e);
    }
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  private ResponseEntity<?> handleRegistrationException(RegistrationException e) {
    if (e instanceof UserDataIntegrityException) {
      return handleUserDataIntegrityException(e);
    }
    if (e instanceof UserCredentialsConditionsException) {
      return handleUserCredentialsConditionsException(e);
    }
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  private ResponseEntity<?> handleUserDataIntegrityException(RegistrationException e) {
    return new ErrorResponseBuilder()
        .error("CONFLICT")
        .message(e.getMessage())
        .status(HttpStatus.CONFLICT)
        .details(new ArrayList<>())
        .build();
  }

  private ResponseEntity<?> handleUserCredentialsConditionsException(RegistrationException e) {
    ArrayList<String> details = new ArrayList<>();
    if (e instanceof PasswordConditionsException) {
      details = registerService.getPasswordConditions();
    }
    if (e instanceof EmailConditionsException) {
      details = registerService.getEmailConditions();
    }
    return new ErrorResponseBuilder()
        .error("INVALID_INPUT")
        .message(e.getMessage())
        .status(HttpStatus.BAD_REQUEST)
        .details(details)
        .build();
  }
}
