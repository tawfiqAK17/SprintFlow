package com.ensa.SprintFlow.exception.generalException;

import com.ensa.SprintFlow.builder.ErrorResponseBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResourceExpiredException extends GeneralException {

  public ResourceExpiredException(String message) {
    super(message);
  }

  @Override
  public ResponseEntity<?> getErrorResponse() {
    return new ErrorResponseBuilder()
        .error("RESOURCE_EXPIRED")
        .message(this.getMessage())
        .status(HttpStatus.GONE)
        .build();
  }
}
