package com.ensa.SprintFlow.exception.generalException;

import com.ensa.SprintFlow.builder.ErrorResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class DataIntegrityViolationException extends GeneralException {

  public DataIntegrityViolationException(String message) {
    super(message);
  }

  @Override
  public ResponseEntity<?> getErrorResponse() {
    return new ErrorResponseBuilder()
        .error("CONFLICT")
        .message(this.getMessage())
        .status(HttpStatus.CONFLICT)
        .build();
  }
}
