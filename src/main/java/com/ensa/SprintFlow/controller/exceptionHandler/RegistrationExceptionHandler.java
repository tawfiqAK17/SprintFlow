package com.ensa.SprintFlow.controller.exceptionHandler;

import com.ensa.SprintFlow.builder.ErrorResponseBuilder;
import com.ensa.SprintFlow.exception.registrationException.RegistrationException;
import com.ensa.SprintFlow.exception.registrationException.userCredentialsConditionsException.EmailConditionsException;
import com.ensa.SprintFlow.exception.registrationException.userCredentialsConditionsException.PasswordConditionsException;
import com.ensa.SprintFlow.exception.registrationException.userCredentialsConditionsException.UserCredentialsConditionsException;
import com.ensa.SprintFlow.exception.registrationException.userDataIntegrityException.UserDataIntegrityException;
import com.ensa.SprintFlow.exception.registrationException.verificationCodeException.VerificationCodeException;
import com.ensa.SprintFlow.exception.registrationException.verificationCodeException.VerificationCodeExpiredException;
import com.ensa.SprintFlow.exception.registrationException.verificationCodeException.VerificationCodeNotFoundException;
import com.ensa.SprintFlow.service.security.RegisterService;
import java.util.ArrayList;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RegistrationExceptionHandler {
  RegisterService registerService;

  RegistrationExceptionHandler(RegisterService registerService) {
    this.registerService = registerService;
  }

  @ExceptionHandler(RegistrationException.class)
  public ResponseEntity<?> handleRegistrationException(RegistrationException e) {
    if (e instanceof UserDataIntegrityException) {
      return handleUserDataIntegrityException(e);
    }
    if (e instanceof UserCredentialsConditionsException) {
      return handleUserCredentialsConditionsException(e);
    }
    if (e instanceof VerificationCodeException) {
      return handleVerificationCodeException(e);
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("unknown server error");
  }

  private ResponseEntity<?> handleVerificationCodeException(RegistrationException e) {
    String error = "";
    HttpStatus status = HttpStatus.NOT_FOUND;
    if (e instanceof VerificationCodeNotFoundException) {
      error = "VERIFICATION_CODE_NOT_FOUND";
      status = HttpStatus.NOT_FOUND;
    }
    if (e instanceof VerificationCodeExpiredException) {
      error = "RESOURCE_EXPIRED";
      status = HttpStatus.GONE;
    }
    return new ErrorResponseBuilder()
        .error(error)
        .message(e.getMessage())
        .status(status)
        .details(new ArrayList<>())
        .build();
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
