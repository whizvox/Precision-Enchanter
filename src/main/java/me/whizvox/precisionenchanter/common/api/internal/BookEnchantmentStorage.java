package me.whizvox.precisionenchanter.common.api.internal;

import me.whizvox.precisionenchanter.common.api.IEnchantmentStorage;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

import java.util.Map;

public class BookEnchantmentStorage implements IEnchantmentStorage {

  @Override
  public boolean accepts(ItemStack stack) {
    return stack.is(Items.BOOK);
  }

  @Override
  public boolean canApply(ItemStack stack, EnchantmentInstance instance) {
    return true;
  }

  @Override
  public Map<Enchantment, Integer> getEnchantments(ItemStack stack) {
    return Map.of();
  }

  @Override
  public ItemStack applyEnchantment(ItemStack origStack, EnchantmentInstance instance) {
    return EnchantedBookItem.createForEnchantment(instance);
  }

  @Override
  public ItemStack removeEnchantment(ItemStack origStack, Enchantment enchantment) {
    return ItemStack.EMPTY;
  }
}
