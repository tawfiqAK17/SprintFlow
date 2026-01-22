package com.ensa.SprintFlow.exception.generalException;

import com.ensa.SprintFlow.builder.ErrorResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ForbiddenExeption extends GeneralException {

  public ForbiddenExeption(String message) {
    super(message);
  }

  @Override
  public ResponseEntity<?> getErrorResponse() {
    return new ErrorResponseBuilder()
        .error("FORBIDDEN")
        .message(this.getMessage())
        .status(HttpStatus.FORBIDDEN)
        .build();
  }
}
