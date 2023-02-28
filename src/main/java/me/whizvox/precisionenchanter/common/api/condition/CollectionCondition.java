package me.whizvox.precisionenchanter.common.api.condition;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class CollectionCondition implements Condition {

  protected final List<Condition> terms;

  public CollectionCondition(List<Condition> terms) {
    this.terms = terms;
  }

  public boolean hasTerms() {
    return !terms.isEmpty();
  }

  public static <C extends CollectionCondition> Codec<C> createCodec(Function<List<Condition>, C> constructor, String key) {
    return new Codec<>() {
      @Override
      public void encode(ConditionCodecContext ctx, C condition, JsonObject out) {
        JsonArray termsArray = new JsonArray(condition.terms.size());
        condition.terms.forEach(c -> {
          JsonObject termObj = ctx.encode(c);
          termsArray.add(termObj);
        });
        out.add(key, termsArray);
      }
      @Override
      public C decode(ConditionCodecContext ctx, JsonObject in) {
        List<Condition> terms = new ArrayList<>();
        JsonArray termsArray = in.get(key).getAsJsonArray();
        termsArray.forEach(termElem -> {
          JsonObject termObj = termElem.getAsJsonObject();
          terms.add(ctx.parse(termObj));
        });
        return constructor.apply(terms);
      }
    };
  }

  public static <C extends CollectionCondition> Codec<C> createCodec(Function<List<Condition>, C> constructor) {
    return createCodec(constructor, "terms");
  }

}
