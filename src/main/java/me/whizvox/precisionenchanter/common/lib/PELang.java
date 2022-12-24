package me.whizvox.precisionenchanter.common.lib;

import me.whizvox.precisionenchanter.common.PrecisionEnchanter;
import net.minecraft.network.chat.Component;

public class PELang {

  public static class Keys {
    public static final String
        CREATIVE_MODE_TAB = "itemGroup." + PrecisionEnchanter.MOD_ID + ".main",
        ENCHANTERS_WORKBENCH = "container." + PrecisionEnchanter.MOD_ID + ".enchanters_workbench",
        SCREEN_ENCHANTERS_WORKBENCH_SELECT_PREV = "screen.enchanters_workbench.select_previous",
        SCREEN_ENCHANTERS_WORKBENCH_SELECT_NEXT = "screen.enchanters_workbench.select_next";
  }

  public static final Component
      CREATIVE_MODE_TAB = Component.translatable(Keys.CREATIVE_MODE_TAB),
      ENCHANTERS_WORKBENCH = Component.translatable(Keys.ENCHANTERS_WORKBENCH),
      SCREEN_ENCHANTERS_WORKBENCH_SELECT_PREV = Component.translatable(Keys.SCREEN_ENCHANTERS_WORKBENCH_SELECT_PREV),
      SCREEN_ENCHANTERS_WORKBENCH_SELECT_NEXT = Component.translatable(Keys.SCREEN_ENCHANTERS_WORKBENCH_SELECT_NEXT);

}
