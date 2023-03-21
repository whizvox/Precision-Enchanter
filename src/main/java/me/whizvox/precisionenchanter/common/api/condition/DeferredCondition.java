package me.whizvox.precisionenchanter.common.api.condition;

public interface DeferredCondition extends Condition {

  boolean test();

  @Override
  default boolean shouldDefer() {
    return true;
  }

  @Override
  default boolean test(LoadStage stage) {
    return switch (stage) {
      case LOAD -> true;
      case POST_LOAD -> test();
    };
  }

}
