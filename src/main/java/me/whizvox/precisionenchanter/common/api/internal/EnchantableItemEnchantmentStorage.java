package me.whizvox.precisionenchanter.common.api.internal;

import me.whizvox.precisionenchanter.common.api.IEnchantmentStorage;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.Map;

public class EnchantableItemEnchantmentStorage implements IEnchantmentStorage {

  @Override
  public boolean accepts(ItemStack stack) {
    return stack.getItem().isEnchantable(stack);
  }

  @Override
  public boolean canApply(ItemStack stack, EnchantmentInstance instance) {
    return instance.enchantment.canEnchant(stack) &&
        getEnchantments(stack).getOrDefault(instance.enchantment, 0) < instance.level;
  }

  @Override
  public Map<Enchantment, Integer> getEnchantments(ItemStack stack) {
    return EnchantmentHelper.getEnchantments(stack);
  }

  @Override
  public ItemStack applyEnchantment(ItemStack origStack, EnchantmentInstance instance) {
    ItemStack copy = origStack.copy();
    copy.setCount(1);
    var enchantments = EnchantmentHelper.getEnchantments(copy);
    enchantments.put(instance.enchantment, instance.level);
    EnchantmentHelper.setEnchantments(enchantments, copy);
    return copy;
  }

  @Override
  public ItemStack removeEnchantment(ItemStack origStack, Enchantment enchantment) {
    ItemStack copy = origStack.copy();
    copy.setCount(1);
    var enchantments = EnchantmentHelper.getEnchantments(copy);
    enchantments.remove(enchantment);
    EnchantmentHelper.setEnchantments(enchantments, copy);
    return copy;
  }

}
