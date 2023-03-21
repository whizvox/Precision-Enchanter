package me.whizvox.precisionenchanter.common.api.condition;

import com.google.gson.JsonObject;

public record NotCondition(Condition term) implements Condition {

  @Override
  public boolean test(LoadStage stage) {
    if (shouldDefer() && stage == LoadStage.LOAD) {
      return true;
    }
    return !term.test(stage);
  }

  @Override
  public boolean shouldDefer() {
    return term.shouldDefer();
  }

  public static final Codec<NotCondition> CODEC = new Codec<>() {

    @Override
    public void encode(ConditionCodecContext ctx, NotCondition condition, JsonObject out) {
      JsonObject termObj = ctx.encode(condition.term);
      out.add("term", termObj);
    }

    @Override
    public NotCondition decode(ConditionCodecContext ctx, JsonObject in) {
      JsonObject termObj = in.getAsJsonObject("term");
      return new NotCondition(ctx.parse(termObj));
    }

  };

}
