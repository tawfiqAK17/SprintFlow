package com.ensa.SprintFlow.exception;

import org.springframework.http.ResponseEntity;

public abstract class ApplicationException extends RuntimeException {

  public ApplicationException(String message) {
    super(message);
  }

  public abstract ResponseEntity<?> getErrorResponse();
}
