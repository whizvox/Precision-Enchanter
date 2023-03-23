package me.whizvox.precisionenchanter.common.api.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;

public interface Condition {

  boolean test(LoadStage stage);

  /**
   * There are some conditions where testing should not happen during a data load, but afterwards. If this condition's
   * test is to be "deferred", then a kind of lazy testing is done instead, where the test is stored, rather than acted
   * upon, during data loading. Later, when some procedure needs to access the enchantment recipe this condition is
   * bound to, that is when all deferred tests are done.
   * @return <code>false</code> if {@link #test(LoadStage)} is called and acted upon during data loading, or <code>true</code>
   * should the test instead be deferred until later.
   */
  default boolean shouldDefer() {
    return false;
  }

  interface Codec<C extends Condition> {

    void encode(ConditionCodecContext ctx, C condition, JsonObject out);

    C decode(ConditionCodecContext ctx, JsonObject in);

  }

  Condition TAUTOLOGY = (stage) -> true;

  Condition CONTRADICTION = (stage) -> false;

  static Condition not(Condition condition) {
    return new NotCondition(condition);
  }

  static Condition and(Condition... conditions) {
    return new AndCondition(Arrays.asList(conditions));
  }

  static Condition or(Condition... conditions) {
    return new OrCondition(Arrays.asList(conditions));
  }

  static Condition xor(Condition... conditions) {
    return new XorCondition(Arrays.asList(conditions));
  }

  static Condition modLoaded(String modId) {
    return new ModLoadedCondition(modId);
  }

  static Condition cofhEnchantmentEnabled(ResourceLocation enchantmentId) {
    return new CoFHEnabledCondition(enchantmentId);
  }

  static Condition cofhEnchantmentEnabled(Enchantment enchantment) {
    return cofhEnchantmentEnabled(ForgeRegistries.ENCHANTMENTS.getKey(enchantment));
  }

}
