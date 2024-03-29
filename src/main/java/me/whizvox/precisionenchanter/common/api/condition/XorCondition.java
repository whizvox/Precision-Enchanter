package me.whizvox.precisionenchanter.common.api.condition;

import java.util.List;

public class XorCondition extends CollectionCondition {

  public XorCondition(List<Condition> terms) {
    super(terms);
  }

  @Override
  public boolean test(LoadStage stage) {
    if (!hasTerms()) {
      return false;
    }
    boolean result = terms.get(0).test(stage);
    for (int i = 1; i < terms.size(); i++) {
      if (result != terms.get(i).test(stage)) {
        return false;
      }
    }
    return true;
  }

  public static final Codec<XorCondition> CODEC = CollectionCondition.createCodec(XorCondition::new);

}
