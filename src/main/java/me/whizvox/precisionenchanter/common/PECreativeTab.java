package me.whizvox.precisionenchanter.common;

import me.whizvox.precisionenchanter.PrecisionEnchanter;
import me.whizvox.precisionenchanter.common.registry.PEItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class PECreativeTab extends CreativeModeTab {

  PECreativeTab() {
    super(PrecisionEnchanter.MOD_ID + ".main");
  }

  @Override
  public ItemStack makeIcon() {
    return new ItemStack(PEItems.ENCHANTERS_WORKBENCH.get());
  }

  public static final CreativeModeTab INSTANCE = new PECreativeTab();

}
