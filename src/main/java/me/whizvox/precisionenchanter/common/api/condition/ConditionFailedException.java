package me.whizvox.precisionenchanter.common.api.condition;

public class ConditionFailedException extends RuntimeException {

  public ConditionFailedException() {
  }

  public ConditionFailedException(String message) {
    super(message);
  }

  public ConditionFailedException(String message, Throwable cause) {
    super(message, cause);
  }

  public ConditionFailedException(Throwable cause) {
    super(cause);
  }

}
