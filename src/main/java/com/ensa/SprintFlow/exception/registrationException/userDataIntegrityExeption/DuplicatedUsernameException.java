package com.ensa.SprintFlow.exception.registrationException.userDataIntegrityExeption;

public class DuplicatedUsernameException extends UserDataIntegrityExeption {
  public DuplicatedUsernameException() {
    super("this user name already exist");
  }
}
