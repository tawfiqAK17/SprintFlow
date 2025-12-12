package com.ensa.SprintFlow.exception.registrationException.userDataIntegrityException;

public class DuplicatedUsernameException extends UserDataIntegrityException {
  public DuplicatedUsernameException() {
    super("this user name already exist");
  }
}
