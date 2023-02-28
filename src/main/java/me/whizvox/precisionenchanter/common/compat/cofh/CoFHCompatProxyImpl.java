package me.whizvox.precisionenchanter.common.compat.cofh;

import cofh.lib.enchantment.EnchantmentCoFH;
import net.minecraft.world.item.enchantment.Enchantment;

class CoFHCompatProxyImpl extends CoFHCompatProxy {

  @Override
  public boolean isEnchantmentEnabled(Enchantment enchantment) {
    return !(enchantment instanceof EnchantmentCoFH cofhEnchantment) || cofhEnchantment.isEnabled();
  }

}
