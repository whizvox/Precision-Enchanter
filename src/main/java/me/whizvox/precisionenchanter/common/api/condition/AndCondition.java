package me.whizvox.precisionenchanter.common.api.condition;

import java.util.List;

public class AndCondition extends CollectionCondition {

  public AndCondition(List<Condition> terms) {
    super(terms);
  }

  @Override
  public boolean test(LoadStage stage) {
    return terms.stream().allMatch(condition -> condition.test(stage));
  }

  public static final Codec<AndCondition> CODEC = CollectionCondition.createCodec(AndCondition::new);

}
