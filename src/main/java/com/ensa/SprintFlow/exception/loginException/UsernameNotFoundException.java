package com.ensa.SprintFlow.exception.loginException;

public class UsernameNotFoundException extends LoginException {

  public UsernameNotFoundException() {
    super("the username does not exist");
  }
}
