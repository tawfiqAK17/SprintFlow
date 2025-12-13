package com.ensa.SprintFlow.controller.exceptionHandler;

import com.ensa.SprintFlow.builder.ErrorResponseBuilder;
import com.ensa.SprintFlow.exception.loginException.IncorrectPasswordException;
import com.ensa.SprintFlow.exception.loginException.LoginException;
import com.ensa.SprintFlow.exception.loginException.UserNotVerifiedException;
import com.ensa.SprintFlow.exception.loginException.UsernameNotFoundException;
import java.util.ArrayList;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoginExceptionHandler {

  @ExceptionHandler(LoginException.class)
  public ResponseEntity<?> handleLoginException(LoginException e) {
    if (e instanceof UsernameNotFoundException) {
      return handleUsernameNotFoundException(e);
    }
    if (e instanceof IncorrectPasswordException) {
      return handleIncorrectPasswordException(e);
    }
    if (e instanceof UserNotVerifiedException) {
      return handleUserNotVerifiedException(e);
    }
    return null;
  }

  private ResponseEntity<?> handleUserNotVerifiedException(LoginException e) {
    return new ErrorResponseBuilder()
        .error("USER_NOT_VERIFIED")
        .message(e.getMessage())
        .status(HttpStatus.FORBIDDEN)
        .details(new ArrayList<>())
        .build();
  }

  private ResponseEntity<?> handleIncorrectPasswordException(LoginException e) {

    return new ErrorResponseBuilder()
        .error("INCORRECT_PASSWORD")
        .message(e.getMessage())
        .status(HttpStatus.UNAUTHORIZED)
        .details(new ArrayList<>())
        .build();
  }

  private ResponseEntity<?> handleUsernameNotFoundException(LoginException e) {
    return new ErrorResponseBuilder()
        .error("USERNAME_NOT_FOUND")
        .message(e.getMessage())
        .status(HttpStatus.CONFLICT)
        .details(new ArrayList<>())
        .build();
  }
}
