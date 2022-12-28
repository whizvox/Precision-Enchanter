package me.whizvox.precisionenchanter.common.api.internal;

import me.whizvox.precisionenchanter.common.api.IEnchantmentStorage;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.Map;

public class EnchantedBookEnchantmentStorage implements IEnchantmentStorage {

  @Override
  public boolean accepts(ItemStack stack) {
    return stack.is(Items.ENCHANTED_BOOK);
  }

  @Override
  public boolean canApply(ItemStack stack, EnchantmentInstance instance) {
    return getEnchantments(stack).getOrDefault(instance.enchantment, 0) < instance.level;
  }

  @Override
  public Map<Enchantment, Integer> getEnchantments(ItemStack stack) {
    return EnchantmentHelper.getEnchantments(stack);
  }

  @Override
  public ItemStack applyEnchantment(ItemStack origStack, EnchantmentInstance instance) {
    ItemStack result = origStack.copy();
    EnchantedBookItem.addEnchantment(result, instance);
    return result;
  }

  @Override
  public ItemStack removeEnchantment(ItemStack origStack, Enchantment enchantment) {
    Map<Enchantment, Integer> enchantments = getEnchantments(origStack);
    enchantments.remove(enchantment);
    if (enchantments.isEmpty()) {
      return new ItemStack(Items.BOOK);
    }
    ItemStack result = new ItemStack(Items.ENCHANTED_BOOK);
    EnchantmentHelper.setEnchantments(enchantments, result);
    return result;
  }

}
