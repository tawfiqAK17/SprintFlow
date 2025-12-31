package com.ensa.SprintFlow.exception.generalException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ensa.SprintFlow.builder.ErrorResponseBuilder;
import com.ensa.SprintFlow.exception.ApplicationException;
import com.ensa.SprintFlow.security.service.EmailService;

public class EmailConditionsException extends ApplicationException {
  public EmailConditionsException() {
    super("the password does not satisfies the conditions");
  }

  @Override
  public ResponseEntity<?> getErrorResponse() {
    return new ErrorResponseBuilder()
        .error("INVALID_INPUT")
        .message(this.getMessage())
        .status(HttpStatus.BAD_REQUEST)
        .details(EmailService.getConditions())
        .build();
  }
}
