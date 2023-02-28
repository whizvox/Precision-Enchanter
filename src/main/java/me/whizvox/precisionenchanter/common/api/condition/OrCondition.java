package me.whizvox.precisionenchanter.common.api.condition;

import java.util.List;

public class OrCondition extends CollectionCondition {

  public OrCondition(List<Condition> terms) {
    super(terms);
  }

  @Override
  public boolean test() {
    return terms.stream().anyMatch(Condition::test);
  }

  public static final Codec<OrCondition> CODEC = CollectionCondition.createCodec(OrCondition::new);

}
