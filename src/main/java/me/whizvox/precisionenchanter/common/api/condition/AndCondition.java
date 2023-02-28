package me.whizvox.precisionenchanter.common.api.condition;

import java.util.List;

public class AndCondition extends CollectionCondition {

  public AndCondition(List<Condition> terms) {
    super(terms);
  }

  @Override
  public boolean test() {
    return terms.stream().allMatch(Condition::test);
  }

  public static final Codec<AndCondition> CODEC = CollectionCondition.createCodec(AndCondition::new);

}
