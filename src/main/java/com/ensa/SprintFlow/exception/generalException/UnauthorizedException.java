package com.ensa.SprintFlow.exception.generalException;

import com.ensa.SprintFlow.builder.ErrorResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UnauthorizedException extends GeneralException {

  public UnauthorizedException(String message) {
    super(message);
  }

  @Override
  public ResponseEntity<?> getErrorResponse() {

    return new ErrorResponseBuilder()
        .error("UNAUTHORIZED_ACCESS")
        .message(this.getMessage())
        .status(HttpStatus.UNAUTHORIZED)
        .build();
  }
}
