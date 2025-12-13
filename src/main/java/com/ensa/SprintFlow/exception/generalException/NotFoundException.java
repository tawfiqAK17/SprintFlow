package com.ensa.SprintFlow.exception.generalException;

import com.ensa.SprintFlow.builder.ErrorResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class NotFoundException extends GeneralException {

  public NotFoundException(String message) {
    super(message);
  }

  @Override
  public ResponseEntity<?> getErrorResponse() {
    return new ErrorResponseBuilder()
        .error("NOT_FOUND")
        .message(this.getMessage())
        .status(HttpStatus.NOT_FOUND)
        .build();
  }
}
