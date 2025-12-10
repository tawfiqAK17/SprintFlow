package com.ensa.SprintFlow.exception.registrationException;

public class DuplicatedUsernameException extends RegistrationException {
  public DuplicatedUsernameException() {
    super("this user name already exist");
  }
}
