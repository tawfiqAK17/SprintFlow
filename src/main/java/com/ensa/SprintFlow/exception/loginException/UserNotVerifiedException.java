package com.ensa.SprintFlow.exception.loginException;

public class UserNotVerifiedException extends LoginException {

	public UserNotVerifiedException() {
		super("the user is not verified yet");
	}

  
}
