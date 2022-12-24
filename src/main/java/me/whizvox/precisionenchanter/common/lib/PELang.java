package me.whizvox.precisionenchanter.common.lib;

import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import net.minecraft.network.chat.Component;

public class PELang {

  public static class Keys {
    public static final String
        CREATIVE_MODE_TAB = "itemGroup." + PrecisionEnchanter.MOD_ID + ".main",
        PRECISION_ENCHANTMENT_TABLE = "container." + PrecisionEnchanter.MOD_ID + ".precision_enchantment_table",
        SCREEN_ENCHANTERS_WORKBENCH_SELECT_UP = "screen.enchanters_workbench.select_up",
        SCREEN_ENCHANTERS_WORKBENCH_SELECT_DOWN = "screen.enchanters_workbench.select_down";
  }

  public static final Component
      CREATIVE_MODE_TAB = Component.translatable(Keys.CREATIVE_MODE_TAB),
      PRECISION_ENCHANTMENT_TABLE = Component.translatable(Keys.PRECISION_ENCHANTMENT_TABLE),
      SCREEN_ENCHANTERS_WORKBENCH_SELECT_UP = Component.translatable(Keys.SCREEN_ENCHANTERS_WORKBENCH_SELECT_UP),
      SCREEN_ENCHANTERS_WORKBENCH_SELECT_DOWN = Component.translatable(Keys.SCREEN_ENCHANTERS_WORKBENCH_SELECT_DOWN);

}
