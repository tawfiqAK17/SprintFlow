package com.ensa.SprintFlow.enums;

public enum TaskStatus {
  TODO(1),
  IN_PROGRESS(2),
  TO_BE_TESTED(3),
  TESTED(4),
  TEST_FAILED(4),
  DONE(5);

  private final int value;

  TaskStatus( int value){
      this.value = value;
  }

  public int getValue(){
    return value;
  }
}
