package me.whizvox.precisionenchanter.common.api.condition;

import com.google.gson.JsonObject;
import me.whizvox.precisionenchanter.common.compat.cofh.CoFHCompatProxy;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.ForgeRegistries;

public class CoFHEnabledCondition implements Condition {

  private final ResourceLocation enchantmentId;

  public CoFHEnabledCondition(ResourceLocation enchantmentId) {
    this.enchantmentId = enchantmentId;
  }

  @Override
  public boolean test() {
    Enchantment enchantment = ForgeRegistries.ENCHANTMENTS.getValue(enchantmentId);
    if (enchantment != null) {
      return CoFHCompatProxy.getInstance().isEnchantmentEnabled(enchantment);
    }
    return false;
  }

  public static final Codec<CoFHEnabledCondition> CODEC = new Codec<>() {

    @Override
    public void encode(ConditionCodecContext ctx, CoFHEnabledCondition condition, JsonObject out) {
      out.addProperty("enchantment", condition.enchantmentId.toString());
    }

    @Override
    public CoFHEnabledCondition decode(ConditionCodecContext ctx, JsonObject in) {
      ResourceLocation enchantmentId = new ResourceLocation(in.get("enchantment").getAsString());
      return new CoFHEnabledCondition(enchantmentId);
    }

  };

}
