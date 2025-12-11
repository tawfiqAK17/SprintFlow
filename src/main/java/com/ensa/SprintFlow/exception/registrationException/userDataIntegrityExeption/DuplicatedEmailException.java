package com.ensa.SprintFlow.exception.registrationException.userDataIntegrityExeption;

public class DuplicatedEmailException extends UserDataIntegrityExeption {
  public DuplicatedEmailException() {
    super("this email already exist");
  }
}
