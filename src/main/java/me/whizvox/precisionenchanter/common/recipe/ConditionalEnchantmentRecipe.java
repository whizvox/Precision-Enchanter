package me.whizvox.precisionenchanter.common.recipe;

import com.google.gson.*;
import me.whizvox.precisionenchanter.common.api.condition.Condition;
import me.whizvox.precisionenchanter.common.api.condition.ConditionFailedException;
import me.whizvox.precisionenchanter.common.lib.PEConditionCodecContext;
import me.whizvox.precisionenchanter.common.lib.PELog;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ConditionalEnchantmentRecipe extends EnchantmentRecipe {

  public final Condition condition;

  public ConditionalEnchantmentRecipe(EnchantmentRecipe recipe, Condition condition) {
    super(recipe);
    this.condition = condition;
  }

  public ConditionalEnchantmentRecipe(ConditionalEnchantmentRecipe other) {
    this(other, other.condition);
  }

  @Override
  public ConditionalEnchantmentRecipe immutable() {
    return this instanceof Immutable ? this : new Immutable(this);
  }

  public EnchantmentRecipe nonConditional() {
    return new EnchantmentRecipe(this);
  }

  public static class Immutable extends ConditionalEnchantmentRecipe {

    public Immutable(ConditionalEnchantmentRecipe recipe) {
      super(recipe);
      if (isInvalid() && getId() != null) {
        List<String> problems = new ArrayList<>(2);
        if (getIngredients().isEmpty()) {
          problems.add("no ingredients");
        }
        if (getEnchantment() == null) {
          problems.add("result not defined");
        }
        PELog.LOGGER.warn(PELog.M_SERVER, "Invalid precision enchantment recipe ({}): {}", getId(), String.join(", ", problems));
      }
    }

    @Override
    public boolean isInvalid() {
      return super.isInvalid() || getId() == null;
    }

    @Override
    public void setId(ResourceLocation id) {
    }

    @Override
    public EnchantmentRecipe nonConditional() {
      return new EnchantmentRecipe.Immutable(this);
    }

  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder extends EnchantmentRecipe.Builder {
    private Condition condition;
    public Builder() {
      condition = Condition.TAUTOLOGY;
    }
    @Override
    public Builder id(ResourceLocation id) {
      super.id(id);
      return this;
    }
    @Override
    public Builder ingredient(Ingredient ingredient, int count) {
      super.ingredient(ingredient, count);
      return this;
    }
    @Override
    public Builder ingredient(Ingredient ingredient) {
      super.ingredient(ingredient);
      return this;
    }
    @Override
    public Builder ingredient(ItemLike item, int count) {
      super.ingredient(item, count);
      return this;
    }
    @Override
    public Builder ingredient(ItemLike item) {
      super.ingredient(item);
      return this;
    }
    @Override
    public Builder ingredient(TagKey<Item> tag, int count) {
      super.ingredient(tag, count);
      return this;
    }
    @Override
    public Builder ingredient(TagKey<Item> tag) {
      super.ingredient(tag);
      return this;
    }
    @Override
    public Builder cost(int cost) {
      super.cost(cost);
      return this;
    }
    @Override
    public Builder noCost() {
      super.noCost();
      return this;
    }
    @Override
    public Builder standardCost() {
      super.standardCost();
      return this;
    }
    @Override
    public Builder result(Enchantment result, int level) {
      super.result(result, level);
      return this;
    }
    public Builder condition(Condition condition) {
      this.condition = condition;
      return this;
    }
    @Override
    public ConditionalEnchantmentRecipe buildMutable() {
      return new ConditionalEnchantmentRecipe(super.buildMutable(), condition);
    }
    @Override
    public ConditionalEnchantmentRecipe build() {
      return buildMutable().immutable();
    }
  }

  public static final class Serializer implements JsonSerializer<ConditionalEnchantmentRecipe>, JsonDeserializer<ConditionalEnchantmentRecipe> {

    @Override
    public ConditionalEnchantmentRecipe deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
      JsonObject obj = json.getAsJsonObject();
      Condition condition = PEConditionCodecContext.INSTANCE.parseAllConditions(obj);
      // don't yet test the condition if it's marked as "deferred"
      if (!condition.shouldDefer() && !condition.test()) {
        throw new ConditionFailedException();
      }
      return new ConditionalEnchantmentRecipe(EnchantmentRecipe.SERIALIZER.deserialize(json, type, context), condition);
    }

    @Override
    public JsonElement serialize(ConditionalEnchantmentRecipe recipe, Type type, JsonSerializationContext context) {
      JsonObject obj = new JsonObject();
      PEConditionCodecContext.INSTANCE.writeAllConditions(recipe.condition, obj);
      JsonElement recipeElem = EnchantmentRecipe.SERIALIZER.serialize(recipe, type, context);
      JsonObject recipeObj = recipeElem.getAsJsonObject();
      recipeObj.entrySet().forEach(entry -> obj.add(entry.getKey(), entry.getValue()));
      return obj;
    }

  }

  public static final Serializer SERIALIZER = new Serializer();

}
