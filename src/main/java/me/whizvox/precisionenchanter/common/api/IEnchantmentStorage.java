package me.whizvox.precisionenchanter.common.api;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.Map;

public interface IEnchantmentStorage {

  boolean accepts(ItemStack stack);

  boolean canApply(ItemStack stack, EnchantmentInstance instance);

  Map<Enchantment, Integer> getEnchantments(ItemStack stack);

  ItemStack applyEnchantment(ItemStack origStack, EnchantmentInstance instance);

  default void postApplyEnchantment(ItemStack origStack) {
    origStack.shrink(1);
  }

  ItemStack removeEnchantment(ItemStack origStack, Enchantment enchantment);

  default void postRemoveEnchantment(ItemStack origStack) {
  }

}
