package me.whizvox.precisionenchanter.client.util;

import me.whizvox.precisionenchanter.common.config.PEConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class PEClientUtil {

  public static Component getEnchantmentFullName(Enchantment enchantment, int level) {
    if (PEConfig.CLIENT.displayLevelsAsNumbers.get()) {
      if (level != 1 || enchantment.getMaxLevel() != 1) {
        return Component.translatable(enchantment.getDescriptionId()).append(" " + level);
      }
      return Component.translatable(enchantment.getDescriptionId());
    }
    return enchantment.getFullname(level).copy().withStyle(ChatFormatting.RESET);
  }

  public static Component getEnchantmentFullName(EnchantmentInstance inst) {
    return getEnchantmentFullName(inst.enchantment, inst.level);
  }

}
