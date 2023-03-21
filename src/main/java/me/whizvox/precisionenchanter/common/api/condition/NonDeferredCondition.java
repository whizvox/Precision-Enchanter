package me.whizvox.precisionenchanter.common.api.condition;

public interface NonDeferredCondition extends Condition {

  boolean test();

  @Override
  default boolean test(LoadStage stage) {
    return test();
  }

  @Override
  default boolean shouldDefer() {
    return false;
  }

}
