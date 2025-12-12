package com.ensa.SprintFlow.exception.registrationException.userDataIntegrityException;

public class DuplicatedEmailException extends UserDataIntegrityException {
  public DuplicatedEmailException() {
    super("this email already exist");
  }
}
