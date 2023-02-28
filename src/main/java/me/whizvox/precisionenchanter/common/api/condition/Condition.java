package me.whizvox.precisionenchanter.common.api.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;

public interface Condition {

  boolean test();

  interface Codec<C extends Condition> {

    void encode(ConditionCodecContext ctx, C condition, JsonObject out);

    C decode(ConditionCodecContext ctx, JsonObject in);

  }

  Condition TAUTOLOGY = () -> true;

  Condition CONTRADICTION = () -> false;

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
