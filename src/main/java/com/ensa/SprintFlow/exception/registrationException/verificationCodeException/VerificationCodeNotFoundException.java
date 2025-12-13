package com.ensa.SprintFlow.exception.registrationException.verificationCodeException;

public class VerificationCodeNotFoundException extends VerificationCodeException {

  public VerificationCodeNotFoundException() {
    super("the given verification code is not valid");
  }
}
