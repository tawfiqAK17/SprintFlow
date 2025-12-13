package com.ensa.SprintFlow.exception.generalException;

import com.ensa.SprintFlow.exception.ApplicationException;

public abstract class GeneralException extends ApplicationException {

  public GeneralException(String message) {
    super(message);
  }
}
