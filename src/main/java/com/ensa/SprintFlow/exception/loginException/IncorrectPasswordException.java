package com.ensa.SprintFlow.exception.loginException;

public class IncorrectPasswordException extends LoginException {

  public IncorrectPasswordException() {
    super("the password is incorrect");
  }
}
