package com.ensa.SprintFlow.exception.registrationException.verificationCodeException;

public class VerificationCodeExpiredException extends VerificationCodeException {

  public VerificationCodeExpiredException() {
    super("the given verification code was expired");
  }
}
