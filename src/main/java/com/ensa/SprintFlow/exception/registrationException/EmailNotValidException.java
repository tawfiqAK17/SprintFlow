package com.ensa.SprintFlow.exception.registrationException;

public class EmailNotValidException extends RegistrationException {
  public EmailNotValidException() {
    super("the email is not valid");
  }
}
