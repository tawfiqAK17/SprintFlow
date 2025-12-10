package com.ensa.SprintFlow.exception.registrationException;

public class DuplicatedEmailException extends RegistrationException {
  public DuplicatedEmailException() {
    super("this email already exist");
  }
}
