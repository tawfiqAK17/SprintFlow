package com.ensa.SprintFlow.controller.exceptionHandler;

import com.ensa.SprintFlow.builder.ErrorResponseBuilder;
import com.ensa.SprintFlow.exception.ApplicationException;

import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {

  ErrorResponseBuilder errorResponseBuilder;

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<?> handleHttpMessageNotReadableException( HttpMessageNotReadableException e){
    ErrorResponseBuilder responseBuilder = new ErrorResponseBuilder();
    responseBuilder.error("INVALID_INPUT");
    responseBuilder.status(HttpStatus.BAD_REQUEST);
    responseBuilder.message( "The request body is not valid JSON or it contains invalid fields value.");
    responseBuilder.detail( e.getMessage());
    return responseBuilder.build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    ErrorResponseBuilder responseBuilder = new ErrorResponseBuilder();
    responseBuilder.error("INVALID_INPUT");
    responseBuilder.message("the fields are not valid");
    responseBuilder.status(HttpStatus.BAD_REQUEST);

    e.getBindingResult()
        .getAllErrors()
        .forEach(
            (error) -> {
              responseBuilder.detail(error.getDefaultMessage());
            });
    return responseBuilder.build();
  }

  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<?> handleApplicationException(ApplicationException e) {
    return e.getErrorResponse();
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<?> handleException(RuntimeException e) {
    return errorResponseBuilder
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .error("INTERNAL_SERVER_ERROR")
        .message(e.getMessage())
        .build();
  }
}
