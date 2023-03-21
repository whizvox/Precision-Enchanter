package me.whizvox.precisionenchanter.common.api.condition;

import java.util.List;

public class OrCondition extends CollectionCondition {

  public OrCondition(List<Condition> terms) {
    super(terms);
  }

  @Override
  public boolean test(LoadStage stage) {
    return terms.stream().anyMatch(condition -> condition.test(stage));
  }

  public static final Codec<OrCondition> CODEC = CollectionCondition.createCodec(OrCondition::new);

}
