package com.ensa.SprintFlow.controller.exceptionHandler;

import com.ensa.SprintFlow.builder.ErrorResponseBuilder;
import com.ensa.SprintFlow.exception.registrationException.RegistrationException;
import com.ensa.SprintFlow.exception.registrationException.userCredentialsConditionsException.EmailConditionsException;
import com.ensa.SprintFlow.exception.registrationException.userCredentialsConditionsException.PasswordConditionsException;
import com.ensa.SprintFlow.exception.registrationException.userCredentialsConditionsException.UserCredentialsConditionsException;
import com.ensa.SprintFlow.exception.registrationException.userDataIntegrityException.UserDataIntegrityException;
import com.ensa.SprintFlow.service.security.RegisterService;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RegistrationExceptionHandler {
  RegisterService registerService;

  RegistrationExceptionHandler(RegisterService registerService) {
    this.registerService = registerService;
  }

  @ExceptionHandler(RegistrationException.class)
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
