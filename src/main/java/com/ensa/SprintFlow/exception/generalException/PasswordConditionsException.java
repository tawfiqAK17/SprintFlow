package com.ensa.SprintFlow.exception.generalException;

import com.ensa.SprintFlow.builder.ErrorResponseBuilder;
import com.ensa.SprintFlow.exception.ApplicationException;
import com.ensa.SprintFlow.service.security.PasswordService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PasswordConditionsException extends ApplicationException {
  public PasswordConditionsException() {
    super("the password does not satisfies the conditions");
  }

  @Override
  public ResponseEntity<?> getErrorResponse() {
    return new ErrorResponseBuilder()
        .error("INVALID_INPUT")
        .message(this.getMessage())
        .status(HttpStatus.BAD_REQUEST)
        .details(PasswordService.getConditions())
        .build();
  }
}
